package interstore;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import interstore.DeviceCapability.DeviceCapabilityDto;
//import interstore.XmlStructure.DeviceCapabilityXml;
//import interstore.XmlStructure.ResourceLink;

import java.util.Map;


public class JsonToXmlConverter {

    public static String buildXml(Map<String, Object> data, String rootElement) {
        StringBuilder xml = new StringBuilder();
        xml.append("<").append(rootElement);

        // Add root-level attributes (like xmlns and href) if present
        if (data.containsKey("xmlns")) {
            xml.append(" xmlns=\"").append(data.remove("xmlns")).append("\"");
        }
        if (data.containsKey("href") && !(data.get("href") instanceof Map)) {
            xml.append(" href=\"").append(data.remove("href")).append("\"");
        }

        xml.append(">");

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String tag = entry.getKey();
            Object value = entry.getValue();

            xml.append("<").append(tag);
            if (value instanceof Map) {
                Map<String, Object> attrs = (Map<String, Object>) value;
                for (Map.Entry<String, Object> attr : attrs.entrySet()) {
                    xml.append(" ").append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
                }
                xml.append("/>");
            } else {
                xml.append(">").append(value).append("</").append(tag).append(">");
            }
        }

        xml.append("</").append(rootElement).append(">");
        return xml.toString();
    }


}
