package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import interstore.DeviceCapability.DeviceCapabilityDto;
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
//    public String convertMapToXml(Map<String, Object> map, String obj) throws Exception {
//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.setDefaultUseWrapper(false);
//        if (obj.equals("dcap")){
//            String rawXml = xmlMapper.writeValueAsString(buildDeviceCapabilityFromJson(map));
//            String finalXml = rawXml.replaceAll(" xmlns=\"\"", "");
//            return finalXml;
//        }
//        return null;
//    }

//    public ResourceLink buildResourceLink(Map<String, Object> linkData) {
//        String href = (String) linkData.get("href");
//        Integer all = linkData.containsKey("all") ? (Integer) linkData.get("all") : 0;
//
//        return new ResourceLink(href, all);
//    }
//
//    public DeviceCapabilityXml buildDeviceCapabilityFromJson(Map<String, Object> json) {
//        DeviceCapabilityXml dcap = new DeviceCapabilityXml();
//
//        if (json.containsKey("href")) {
//            dcap.setHref((String) json.get("href"));
//        }
//
//        if (json.containsKey("DemandResponseProgramListLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("DemandResponseProgramListLink");
//            dcap.setDemandResponseProgramListLink(buildResourceLink(link));
//        }
//
//        if (json.containsKey("DERProgramListLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("DERProgramListLink");
//            dcap.setDerProgramListLink(buildResourceLink(link));
//        }
//
//        if (json.containsKey("MessagingProgramListLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("MessagingProgramListLink");
//            dcap.setMessagingProgramListLink(buildResourceLink(link));
//        }
//
//        if (json.containsKey("ResponseSetListLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("ResponseSetListLink");
//            dcap.setResponseSetListLink(buildResourceLink(link));
//        }
//
//        if (json.containsKey("TariffProfileListLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("TariffProfileListLink");
//            dcap.setTariffProfileListLink(buildResourceLink(link));
//        }
//
//        if (json.containsKey("TimeLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("TimeLink");
//            dcap.setTimeLink(buildResourceLink(link));
//        }
//
//        if (json.containsKey("UsagePointListLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("UsagePointListLink");
//            dcap.setUsagePointListLink(buildResourceLink(link));
//        }
//
//        if (json.containsKey("EndDeviceListLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("EndDeviceListLink");
//            dcap.setEndDeviceListLink(buildResourceLink(link));
//        }
//
//        if (json.containsKey("MirrorUsagePointListLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("MirrorUsagePointListLink");
//            dcap.setMirrorUsagePointListLink(buildResourceLink(link));
//        }
//
//        if (json.containsKey("SelfDeviceLink")) {
//            Map<String, Object> link = (Map<String, Object>) json.get("SelfDeviceLink");
//            dcap.setSelfDeviceLink(buildResourceLink(link));
//        }
//
//        return dcap;
//    }

}
