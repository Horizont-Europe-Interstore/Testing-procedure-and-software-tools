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
    private final Map<String, String> wadlExpectedElements = new ConcurrentHashMap<>();
    private final Set<String> pollingEndpoints = Set.of("/dcap", "/tm");

    @Autowired
    private NatsPublisher natsPublisher;

    public XmlValidationService() {
        loadWadlMappings();
    }

    private void loadWadlMappings() {
        try (InputStream is = getClass().getResourceAsStream("sep_wadl.xml")) {
            if (is == null) return;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(is);
            NodeList resources = doc.getElementsByTagName("resource");
            
            for (int i = 0; i < resources.getLength(); i++) {
                Element resource = (Element) resources.item(i);
                String path = resource.getAttribute("wx:samplePath");
                NodeList methods = resource.getElementsByTagName("method");
                
                for (int j = 0; j < methods.getLength(); j++) {
                    Element method = (Element) methods.item(j);
                    String httpMethod = method.getAttribute("name");
                    NodeList responses = method.getElementsByTagName("response");
                    
                    for (int k = 0; k < responses.getLength(); k++) {
                        Element response = (Element) responses.item(k);
                        NodeList reps = response.getElementsByTagName("representation");
                        if (reps.getLength() > 0) {
                            Element rep = (Element) reps.item(0);
                            String element = rep.getAttribute("element");
                            if (element.startsWith("sep:")) {
                                wadlExpectedElements.put(path + ":" + httpMethod, element.substring(4));
                            }
                        }
                    }
                }
            }
            LOGGER.info("Loaded " + wadlExpectedElements.size() + " WADL mappings");
        } catch (Exception e) {
            LOGGER.severe("Failed to load WADL: " + e.getMessage());
        }
    }

    public XmlValidationResult validateXml(String endpoint, String httpMethod, String requestXml, String actualXml) {
        String id = UUID.randomUUID().toString();
        
        if (isPollingEndpoint(endpoint)) {
            LOGGER.info("Skipping validation for polling endpoint: " + endpoint);
            return null;
        }

        try {
            boolean isWellFormed = isWellFormedXml(actualXml);
            String expectedElement = getExpectedElement(endpoint, httpMethod);
            String actualElement = extractRootElement(actualXml);
            
            boolean isValid = isWellFormed && (expectedElement == null || expectedElement.equals(actualElement));
            String differences = buildDifferences(isWellFormed, expectedElement, actualElement);

            XmlValidationResult result = new XmlValidationResult(
                id, endpoint, httpMethod, requestXml, expectedElement,
                actualXml, isValid, differences
            );

            validationResults.put(id, result);
            publishResultToNats(result);
            return result;

        } catch (Exception e) {
            XmlValidationResult errorResult = new XmlValidationResult(
                id, endpoint, httpMethod, requestXml, null,
                actualXml, false, "Error: " + e.getMessage()
            );
            validationResults.put(id, errorResult);
            publishResultToNats(errorResult);
            return errorResult;
        }
    }

    private boolean isPollingEndpoint(String endpoint) {
        return pollingEndpoints.stream().anyMatch(endpoint::startsWith);
    }

    private String getExpectedElement(String endpoint, String httpMethod) {
        String normalized = normalizeEndpoint(endpoint);
        return wadlExpectedElements.get(normalized + ":" + httpMethod);
    }

    private String normalizeEndpoint(String endpoint) {
        return endpoint.replaceAll("/\\d+", "/{id}");
    }

    private String extractRootElement(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            return doc.getDocumentElement().getLocalName();
        } catch (Exception e) {
            return null;
        }
    }

    private String buildDifferences(boolean isWellFormed, String expected, String actual) {
        if (!isWellFormed) return "XML parsing error";
        if (expected == null) return "No WADL mapping found";
        if (expected.equals(actual)) return "Valid - matches WADL";
        return "Expected: " + expected + ", Actual: " + actual;
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
            message.put("timestamp", result.getTimestamp().toString());
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