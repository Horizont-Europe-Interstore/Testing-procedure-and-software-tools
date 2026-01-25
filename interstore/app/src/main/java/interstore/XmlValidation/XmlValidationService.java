package interstore.XmlValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import interstore.NatsPublisher;
import interstore.EndDevice.EndDeviceRepository;
import interstore.EndDevice.EndDeviceEntity;
import interstore.DeviceCapability.DeviceCapabilityRepository;
import interstore.DeviceCapability.DeviceCapabilityEntity;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
public class XmlValidationService {
    
    private final Map<String, XmlValidationResult> validationResults = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(XmlValidationService.class.getName());
    
    @Autowired
    private NatsPublisher natsPublisher;
    
    @Autowired
    private EndDeviceRepository endDeviceRepository;
    
    @Autowired
    private DeviceCapabilityRepository deviceCapabilityRepository;

    @PostConstruct
    public void initialize() {
        LOGGER.info("XML Validation Service initialized - using database comparison mode");
    }

    public XmlValidationResult validateXml(String endpoint, String httpMethod, 
                                         String requestXml, String actualXml) {
        String id = UUID.randomUUID().toString();
        
        try {
            // Parse actual XML to extract values
            Map<String, String> actualValues = parseXmlValues(actualXml);
            
            // Get expected values from database based on endpoint
            Map<String, String> expectedValues = getExpectedValuesFromDatabase(endpoint);
            
            // Compare values and generate differences
            List<String> differences = compareValues(expectedValues, actualValues);
            
            boolean isValid = differences.isEmpty();
            String differencesText = String.join("; ", differences);
            
            XmlValidationResult result = new XmlValidationResult(
                id, endpoint, httpMethod, requestXml, 
                expectedValues.toString(), actualXml, isValid, differencesText
            );
            
            validationResults.put(id, result);
            
            // Publish comparison result to NATS
            publishComparisonResult(result, expectedValues, actualValues, differences);
            
            return result;
            
        } catch (Exception e) {
            LOGGER.severe("XML validation error: " + e.getMessage());
            XmlValidationResult errorResult = new XmlValidationResult(
                id, endpoint, httpMethod, requestXml, "Error validating XML", 
                actualXml, false, "Error: " + e.getMessage()
            );
            validationResults.put(id, errorResult);
            return errorResult;
        }
    }

    private Map<String, String> parseXmlValues(String xml) throws Exception {
        Map<String, String> values = new HashMap<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        
        // Extract link values from XML
        NodeList links = doc.getElementsByTagName("*");
        for (int i = 0; i < links.getLength(); i++) {
            Element element = (Element) links.item(i);
            String tagName = element.getTagName();
            
            if (tagName.endsWith("Link")) {
                String href = element.getAttribute("href");
                if (!href.isEmpty()) {
                    values.put(tagName, href);
                }
            }
            
            // Extract other important values
            if (tagName.equals("sFDI") || tagName.equals("lFDI") || tagName.equals("deviceCategory")) {
                values.put(tagName, element.getTextContent());
            }
        }
        
        return values;
    }
    
    private Map<String, String> getExpectedValuesFromDatabase(String endpoint) {
        Map<String, String> expectedValues = new HashMap<>();
        
        try {
            if (endpoint.contains("/edev")) {
                // Get EndDevice data from database
                Optional<EndDeviceEntity> endDevice = endDeviceRepository.findById(1L);
                if (endDevice.isPresent()) {
                    EndDeviceEntity device = endDevice.get();
                    expectedValues.put("RegistrationLink", device.getRegistrationLink());
                    expectedValues.put("DERListLink", device.getDERListLink());
                    expectedValues.put("DeviceStatusLink", device.getDeviceStatusLink());
                    expectedValues.put("FunctionSetAssignmentsListLink", device.getFunctionSetAssignmentsListLink());
                    expectedValues.put("sFDI", device.getSfdi());
                }
            } else if (endpoint.contains("/dcap")) {
                // Get DeviceCapability data from database
                Optional<DeviceCapabilityEntity> dcap = deviceCapabilityRepository.findById(1L);
                if (dcap.isPresent()) {
                    DeviceCapabilityEntity capability = dcap.get();
                    expectedValues.put("TimeLink", capability.getTimeLink());
                    expectedValues.put("EndDeviceListLink", capability.getEndDeviceListLink());
                    expectedValues.put("SelfDeviceLink", capability.getSelfDeviceLink());
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error getting expected values from database: " + e.getMessage());
        }
        
        return expectedValues;
    }
    
    private List<String> compareValues(Map<String, String> expected, Map<String, String> actual) {
        List<String> differences = new ArrayList<>();
        
        // Check for missing links in actual XML
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = actual.get(key);
            
            if (actualValue == null) {
                differences.add("Missing " + key + " (expected: " + expectedValue + ")");
            } else if (!expectedValue.equals(actualValue)) {
                differences.add(key + " mismatch (expected: " + expectedValue + ", actual: " + actualValue + ")");
            }
        }
        
        // Check for unexpected links in actual XML
        for (Map.Entry<String, String> entry : actual.entrySet()) {
            String key = entry.getKey();
            if (!expected.containsKey(key)) {
                differences.add("Unexpected " + key + " (value: " + entry.getValue() + ")");
            }
        }
        
        return differences;
    }

    public List<XmlValidationResult> getAllValidationResults() {
        return new ArrayList<>(validationResults.values());
    }

    public XmlValidationResult getValidationResult(String id) {
        return validationResults.get(id);
    }

    public void clearResults() {
        validationResults.clear();
    }
    
    private void publishComparisonResult(XmlValidationResult result, Map<String, String> expectedValues, 
                                        Map<String, String> actualValues, List<String> differences) {
        try {
            JSONObject payload = new JSONObject();
            payload.put("validationId", result.getId());
            payload.put("endpoint", result.getEndpoint());
            payload.put("httpMethod", result.getHttpMethod());
            payload.put("isValid", result.isValid());
            payload.put("result", result.isValid() ? "PASS" : "FAIL");
            payload.put("timestamp", result.getTimestamp().toString());
            
            // Add expected values from database
            JSONObject expectedJson = new JSONObject();
            for (Map.Entry<String, String> entry : expectedValues.entrySet()) {
                expectedJson.put(entry.getKey(), entry.getValue());
            }
            payload.put("expectedFromServer", expectedJson);
            
            // Add actual values from device XML
            JSONObject actualJson = new JSONObject();
            for (Map.Entry<String, String> entry : actualValues.entrySet()) {
                actualJson.put(entry.getKey(), entry.getValue());
            }
            payload.put("actualFromDevice", actualJson);
            
            // Add differences
            if (!differences.isEmpty()) {
                payload.put("differences", differences);
            }
            
            // Publish to NATS topic
            natsPublisher.publish("ieee2030.validation.results", payload.toString());
            
            LOGGER.info("Published validation result to NATS: " + result.getEndpoint() + " - " + 
                       (result.isValid() ? "PASS" : "FAIL") + 
                       (differences.isEmpty() ? "" : " (" + differences.size() + " differences)"));
            
        } catch (Exception e) {
            LOGGER.severe("Failed to publish validation result to NATS: " + e.getMessage());
        }
    }
}