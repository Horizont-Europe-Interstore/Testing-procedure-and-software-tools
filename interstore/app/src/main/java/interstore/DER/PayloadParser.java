package interstore.DER;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Utility class to parse both JSON and XML payloads into JSONObject
 */
public class PayloadParser {
    private static final Logger LOGGER = Logger.getLogger(PayloadParser.class.getName());
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * Detects if the payload is JSON or XML and converts it to JSONObject
     * @param payload The raw string payload (JSON or XML)
     * @param contentType The Content-Type header (optional)
     * @return JSONObject representation of the payload
     * @throws Exception if parsing fails
     */
    public static JSONObject parse(String payload, String contentType) throws Exception {
        if (payload == null || payload.trim().isEmpty()) {
            throw new IllegalArgumentException("Payload cannot be null or empty");
        }

        String trimmedPayload = payload.trim();

        // Check if it's XML based on content-type or starting character
        boolean isXml = (contentType != null && contentType.contains("xml")) ||
                        trimmedPayload.startsWith("<");

        if (isXml) {
            LOGGER.info("Detected XML payload, parsing...");
            return parseXmlToJson(trimmedPayload);
        } else {
            LOGGER.info("Detected JSON payload, parsing...");
            return new JSONObject(trimmedPayload);
        }
    }

    /**
     * Parse XML string to JSONObject
     */
    private static JSONObject parseXmlToJson(String xmlString) throws Exception {
        try {
            // Parse XML to JsonNode using Jackson
            JsonNode jsonNode = xmlMapper.readTree(xmlString);

            // Convert JsonNode to JSON string, then to JSONObject
            String jsonString = jsonMapper.writeValueAsString(jsonNode);
            JSONObject result = new JSONObject(jsonString);

            LOGGER.info("Parsed XML to JSON: " + result.toString());
            return result;
        } catch (Exception e) {
            LOGGER.severe("Failed to parse XML: " + e.getMessage());
            throw new Exception("Failed to parse XML payload: " + e.getMessage(), e);
        }
    }

    /**
     * Extract a specific field from DERSettings XML
     * IEEE 2030.5 DERSettings XML format example:
     * <DERSettings xmlns="http://ieee.org/2030.5">
     *   <setMaxW>5000</setMaxW>
     *   <setMaxVA>5500</setMaxVA>
     * </DERSettings>
     */
    public static JSONObject parseDERSettingsXml(String xmlPayload, Long endDeviceId, Long derId) throws Exception {
        JSONObject parsed = parseXmlToJson(xmlPayload);
        JSONObject result = new JSONObject();

        // Add the IDs
        result.put("endDeviceId", endDeviceId);
        result.put("derID", derId);

        // Check if root element is DERSettings
        if (parsed.has("DERSettings")) {
            JSONObject derSettings = parsed.getJSONObject("DERSettings");

            // Copy all DER settings fields
            Iterator<String> keys = derSettings.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                // Skip xmlns and other metadata
                if (!key.startsWith("@") && !key.equals("xmlns")) {
                    result.put(key, derSettings.get(key));
                }
            }
        } else {
            // If no DERSettings wrapper, assume all fields are at root
            Iterator<String> keys = parsed.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.startsWith("@") && !key.equals("xmlns")) {
                    result.put(key, parsed.get(key));
                }
            }
        }

        LOGGER.info("Parsed DERSettings XML to: " + result.toString());
        return result;
    }

    /**
     * Extract a specific field from DERCapability XML
     */
    public static JSONObject parseDERCapabilityXml(String xmlPayload, Long endDeviceId, Long derId) throws Exception {
        JSONObject parsed = parseXmlToJson(xmlPayload);
        JSONObject result = new JSONObject();

        result.put("endDeviceId", endDeviceId);
        result.put("derID", derId);

        if (parsed.has("DERCapability")) {
            JSONObject derCap = parsed.getJSONObject("DERCapability");
            Iterator<String> keys = derCap.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.startsWith("@") && !key.equals("xmlns")) {
                    result.put(key, derCap.get(key));
                }
            }
        } else {
            Iterator<String> keys = parsed.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.startsWith("@") && !key.equals("xmlns")) {
                    result.put(key, parsed.get(key));
                }
            }
        }

        LOGGER.info("Parsed DERCapability XML to: " + result.toString());
        return result;
    }
}
