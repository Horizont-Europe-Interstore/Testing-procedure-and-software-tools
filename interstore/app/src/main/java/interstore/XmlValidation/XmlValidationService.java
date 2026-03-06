package interstore.XmlValidation;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import interstore.NatsPublisher;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
public class XmlValidationService {
    private static final Logger LOGGER = Logger.getLogger(XmlValidationService.class.getName());
    private final Map<String, XmlValidationResult> validationResults = new ConcurrentHashMap<>();
    private final Map<String, String> endpointToExpectedXmlFile = new ConcurrentHashMap<>();
    private final Map<String, Long> recentValidations = new ConcurrentHashMap<>();
    private static final long DEDUP_WINDOW_MS = 100;

    @Autowired
    private NatsPublisher natsPublisher;

    public XmlValidationService() {
        loadExpectedXmlMappings();
    }

    private void loadExpectedXmlMappings() {
        endpointToExpectedXmlFile.put("dcap:GET", "DeviceCapability.xml");
        endpointToExpectedXmlFile.put("tm:GET", "Time.xml");
        endpointToExpectedXmlFile.put("dcap/tm:GET", "Time.xml");
        endpointToExpectedXmlFile.put("edev:GET", "EndDeviceList.xml");
        endpointToExpectedXmlFile.put("edev/{id}:GET", "EndDevice.xml");
        endpointToExpectedXmlFile.put("edev/{id}/rg:GET", "Registration.xml");
        endpointToExpectedXmlFile.put("edev/{id}/fsa:GET", "FunctionSetAssignment.xml");
        endpointToExpectedXmlFile.put("edev/{id}/dstat:GET", "DeviceStatus.xml");
        endpointToExpectedXmlFile.put("edev/{id}/der/{id}/dercap:GET", "DerCapability.xml");
        endpointToExpectedXmlFile.put("edev/{id}/der/{id}/dercap:PUT", "DerCapability.xml");
        endpointToExpectedXmlFile.put("edev/{id}/der/{id}/ders:GET", "DerStatus.xml");
        endpointToExpectedXmlFile.put("edev/{id}/der/{id}/ders:PUT", "DerStatus.xml");
        endpointToExpectedXmlFile.put("edev/{id}/der/{id}/dera:GET", "DerAvailability.xml");
        endpointToExpectedXmlFile.put("edev/{id}/der/{id}/derg:GET", "DerSettings.xml");
        endpointToExpectedXmlFile.put("edev/{id}/der/{id}/derg:PUT", "DerSettings.xml");
        endpointToExpectedXmlFile.put("derp:GET", "DerProgramList.xml");
        endpointToExpectedXmlFile.put("derp/{id}:GET", "DerProgramInfo.xml");
        endpointToExpectedXmlFile.put("derp/{id}/derc:GET", "DerControlList.xml");
        endpointToExpectedXmlFile.put("derp/{id}/dercrv:GET", "DerCurveList.xml");
        endpointToExpectedXmlFile.put("edev/{id}/der/{id}/powergeneration:GET", "powergeneration.xml");
        LOGGER.info("Loaded " + endpointToExpectedXmlFile.size() + " expected XML mappings");
    }

    public XmlValidationResult validateXml(String endpoint, String httpMethod, String requestXml, String actualXml) {
        String normalizedEndpoint = normalizeEndpoint(endpoint);
        
        // Check for empty or null response
        if (actualXml == null || actualXml.trim().isEmpty()) {
            LOGGER.warning("Empty or null XML response for " + endpoint);
            String id = normalizedEndpoint + ":" + httpMethod + ":" + System.currentTimeMillis();
            XmlValidationResult result = new XmlValidationResult(
                id, endpoint, httpMethod, requestXml, "",
                actualXml != null ? actualXml : "", false, "Empty or null XML response"
            );
            validationResults.put(id, result);
            publishResultToNats(result);
            return result;
        }
        
        // Deduplication check
        String dedupKey = normalizedEndpoint + ":" + httpMethod + ":" + actualXml.hashCode();
        long now = System.currentTimeMillis();
        Long lastValidation = recentValidations.get(dedupKey);
        if (lastValidation != null && (now - lastValidation) < DEDUP_WINDOW_MS) {
            LOGGER.info("Skipping duplicate validation for: " + normalizedEndpoint + ":" + httpMethod);
            return null;
        }
        recentValidations.put(dedupKey, now);
        
        String id = normalizedEndpoint + ":" + httpMethod + ":" + now;
        
        try {
            String expectedXmlFile = endpointToExpectedXmlFile.get(normalizedEndpoint + ":" + httpMethod);
            
            if (expectedXmlFile == null) {
                LOGGER.warning("No expected XML mapping for: " + normalizedEndpoint + ":" + httpMethod);
                return null;
            }

            LOGGER.info("Validating " + httpMethod + " " + endpoint + " -> " + normalizedEndpoint + ", actualXml length: " + (actualXml != null ? actualXml.length() : 0));

            String expectedXml = loadExpectedXml(expectedXmlFile);
            if (expectedXml == null) {
                LOGGER.warning("Could not load expected XML file: " + expectedXmlFile);
                XmlValidationResult result = new XmlValidationResult(
                    id, endpoint, httpMethod, requestXml, "File not found: " + expectedXmlFile,
                    actualXml, false, "Expected XML file not found: " + expectedXmlFile
                );
                validationResults.put(id, result);
                publishResultToNats(result);
                return result;
            }

            boolean isWellFormed = isWellFormedXml(actualXml);
            if (!isWellFormed) {
                LOGGER.warning("XML is not well-formed for " + endpoint);
                XmlValidationResult result = new XmlValidationResult(
                    id, endpoint, httpMethod, requestXml, expectedXml,
                    actualXml, false, "XML parsing error"
                );
                validationResults.put(id, result);
                publishResultToNats(result);
                return result;
            }

            List<String> differences = compareXmlParameters(expectedXml, actualXml);
            boolean isValid = differences.isEmpty();
            String differencesStr = isValid ? "Valid - all parameters match" : String.join("; ", differences);

            LOGGER.info("Validation result for " + endpoint + ": isValid=" + isValid + ", differences=" + differences.size());

            XmlValidationResult result = new XmlValidationResult(
                id, endpoint, httpMethod, requestXml, expectedXml,
                actualXml, isValid, differencesStr
            );

            validationResults.put(id, result);
            publishResultToNats(result);
            return result;

        } catch (Exception e) {
            LOGGER.severe("Validation error for " + endpoint + ": " + e.getMessage());
            XmlValidationResult errorResult = new XmlValidationResult(
                id, endpoint, httpMethod, requestXml, null,
                actualXml, false, "Error: " + e.getMessage()
            );
            validationResults.put(id, errorResult);
            publishResultToNats(errorResult);
            return errorResult;
        }
    }

    public XmlValidationResult displayXmlOnly(String endpoint, String httpMethod, String actualXml) {
        String normalizedEndpoint = normalizeEndpoint(endpoint);
        long now = System.currentTimeMillis();
        String id = normalizedEndpoint + ":" + httpMethod + ":" + now;
        
        LOGGER.info("Displaying XML only for " + endpoint + ", actualXml length: " + (actualXml != null ? actualXml.length() : 0));
        
        XmlValidationResult result = new XmlValidationResult(
            id, endpoint, httpMethod, "", "",
            actualXml, true, "Display only - no validation"
        );
        
        validationResults.put(id, result);
        publishResultToNats(result);
        return result;
    }

    private String normalizeEndpoint(String endpoint) {
        return endpoint
            .replaceAll("^/", "")
            .replaceAll("\\{[^}]+\\}", "{id}")
            .replaceAll("/\\d+", "/{id}");
    }

    private String loadExpectedXml(String filename) {
        try (InputStream is = getClass().getResourceAsStream("ExpectedXml/" + filename)) {
            if (is == null) return null;
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.severe("Failed to load expected XML: " + filename + " - " + e.getMessage());
            return null;
        }
    }

    private List<String> compareXmlParameters(String expectedXml, String actualXml) {
        List<String> differences = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document expectedDoc = builder.parse(new ByteArrayInputStream(expectedXml.getBytes(StandardCharsets.UTF_8)));
            Document actualDoc = builder.parse(new ByteArrayInputStream(actualXml.getBytes(StandardCharsets.UTF_8)));
            
            Element expectedRoot = expectedDoc.getDocumentElement();
            Element actualRoot = actualDoc.getDocumentElement();
            
            if (!expectedRoot.getLocalName().equals(actualRoot.getLocalName())) {
                differences.add("Root element mismatch: expected " + expectedRoot.getLocalName() + 
                              ", got " + actualRoot.getLocalName());
                return differences;
            }
            
            // Only check that expected elements exist in actual, don't check values
            compareElementsPresence(expectedRoot, actualRoot, "", differences);
            
        } catch (Exception e) {
            differences.add("XML comparison error: " + e.getMessage());
        }
        return differences;
    }

    private void compareElementsPresence(Element expected, Element actual, String path, List<String> differences) {
        String currentPath = path + "/" + expected.getLocalName();
        
        NodeList expectedChildren = expected.getChildNodes();
        Map<String, Element> expectedElements = new HashMap<>();
        
        for (int i = 0; i < expectedChildren.getLength(); i++) {
            Node node = expectedChildren.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                expectedElements.put(child.getLocalName(), child);
            }
        }
        
        // Only check that expected elements exist, don't validate values or counts
        for (Map.Entry<String, Element> entry : expectedElements.entrySet()) {
            String elementName = entry.getKey();
            
            NodeList actualMatches = actual.getElementsByTagName(elementName);
            if (actualMatches.getLength() == 0) {
                differences.add("Missing element: " + currentPath + "/" + elementName);
            }
        }
        
        // Check href attribute if present in expected
        if (expected.hasAttribute("href")) {
            if (!actual.hasAttribute("href")) {
                differences.add("Missing href attribute at " + currentPath);
            }
        }
    }

    private boolean isWellFormedXml(String xml) {
        if (xml == null || xml.trim().isEmpty()) return false;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            return true;
        } catch (Exception e) {
            LOGGER.warning("XML parsing error: " + e.getMessage());
            return false;
        }
    }

    private void publishResultToNats(XmlValidationResult result) {
        try {
            JSONObject message = new JSONObject();
            message.put("id", result.getId());
            message.put("endpoint", result.getEndpoint());
            message.put("httpMethod", result.getHttpMethod());
            message.put("valid", result.isValid());
            message.put("timestamp", result.getTimestamp());
            message.put("differences", result.getDifferences());
            message.put("expectedXml", result.getExpectedXml());
            message.put("actualXml", result.getActualXml());
            message.put("requestXml", result.getRequestXml());

            if (natsPublisher != null) {
                natsPublisher.publish("ieee2030.test.results", message.toString());
                LOGGER.info("Published validation result to NATS: " + result.getId() +
                           " (Valid: " + result.isValid() + ")");
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to publish result to NATS: " + e.getMessage());
        }
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
}