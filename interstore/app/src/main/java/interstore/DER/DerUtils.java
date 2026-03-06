package interstore.DER;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DerUtils {
    
    private static final Map<Integer, String> DER_CONTROL_TYPES = new LinkedHashMap<>();
    static {
        DER_CONTROL_TYPES.put(0x00000001, "Charge mode");
        DER_CONTROL_TYPES.put(0x00000002, "Discharge mode");
        DER_CONTROL_TYPES.put(0x00000004, "opModConnect");
        DER_CONTROL_TYPES.put(0x00000008, "opModEnergize");
        DER_CONTROL_TYPES.put(0x00000010, "opModFixedPFAbsorbW");
        DER_CONTROL_TYPES.put(0x00000020, "opModFixedPFInjectW");
        DER_CONTROL_TYPES.put(0x00000040, "opModFixedVar");
        DER_CONTROL_TYPES.put(0x00000080, "opModFixedW");
        DER_CONTROL_TYPES.put(0x00000100, "opModFreqDroop");
        DER_CONTROL_TYPES.put(0x00000200, "opModFreqWatt");
        DER_CONTROL_TYPES.put(0x00000400, "opModHFRTMayTrip");
        DER_CONTROL_TYPES.put(0x00000800, "opModHFRTMustTrip");
        DER_CONTROL_TYPES.put(0x00001000, "opModHVRTMayTrip");
        DER_CONTROL_TYPES.put(0x00002000, "opModHVRTMomentaryCessation");
        DER_CONTROL_TYPES.put(0x00004000, "opModHVRTMustTrip");
        DER_CONTROL_TYPES.put(0x00008000, "opModLFRTMayTrip");
        DER_CONTROL_TYPES.put(0x00010000, "opModLFRTMustTrip");
        DER_CONTROL_TYPES.put(0x00020000, "opModLVRTMayTrip");
        DER_CONTROL_TYPES.put(0x00040000, "opModLVRTMomentaryCessation");
        DER_CONTROL_TYPES.put(0x00080000, "opModLVRTMustTrip");
        DER_CONTROL_TYPES.put(0x00100000, "opModMaxLimW");
        DER_CONTROL_TYPES.put(0x00200000, "opModTargetVar");
        DER_CONTROL_TYPES.put(0x00400000, "opModTargetW");
        DER_CONTROL_TYPES.put(0x00800000, "opModVoltVar");
        DER_CONTROL_TYPES.put(0x01000000, "opModVoltWatt");
        DER_CONTROL_TYPES.put(0x02000000, "opModWattPF");
        DER_CONTROL_TYPES.put(0x04000000, "opModWattVar");
        DER_CONTROL_TYPES.put(0x08000000, "opModFixedPF");
    }


    public Map<String, Object> PowerGenerationHandler(Map<String, String> derPowerTests) {
          Map<String, Map<String, String>> derPowerValues = new LinkedHashMap<>();
          ArrayList<String> requiredModes = new ArrayList<>();
          requiredModes.add("opModMaxLimW");
          requiredModes.add("opModVoltWatt");
          requiredModes.add("opModFixedVar");
          requiredModes.add("opModFixedPF");
          
           try {
            if(derPowerTests.containsKey("derCapability") && derPowerTests.containsKey("derSettings"))
            {
                String xmlderCapability = derPowerTests.get("derCapability");
                String xmlderSettings = derPowerTests.get("derSettings");
                Map<String, String> modeValidation = new LinkedHashMap<>();
                if(xmlderCapability.contains("modesSupported"))
                {
                    String modesSupportedHexValue = extractValueHexValue(xmlderCapability, "modesSupported");
                    System.out.println("modesSupported Value: " + modesSupportedHexValue);
                    Map<Integer, String> allDecodedModes = decodeHexBinary(modesSupportedHexValue);
                    
                    for(String requiredMode : requiredModes) {
                        boolean found = allDecodedModes.values().stream().anyMatch(mode -> mode.equals(requiredMode));
                        modeValidation.put(requiredMode, found ? "present" : "absent");
                    }
                 }

                // Always extract values even if empty
                Map<String, String> rtgMaxW = extractNestedValues(xmlderCapability, "rtgMaxW");
                Map<String, String> rtgMaxVA = extractNestedValues(xmlderCapability, "rtgMaxVA");
                Map<String, String> setMaxW = extractNestedValues(xmlderSettings, "setMaxW");
                Map<String, String> setMaxVA = extractNestedValues(xmlderSettings, "setMaxVA");
                
                derPowerValues.put("rtgMaxW", rtgMaxW);
                derPowerValues.put("rtgMaxVA", rtgMaxVA);
                derPowerValues.put("setMaxW", setMaxW);
                derPowerValues.put("setMaxVA", setMaxVA);

                return Map.of("status", "success", "modeValidation", modeValidation, "derPowerValues", derPowerValues, "xmlOnly", true);
            } else {
                return Map.of("status", "error", "message", "Missing required parameters");
            }
           
           } catch (Exception e) {
               return Map.of("status", "error", "message", e.getMessage());
           }
    }
          public Map<Integer, String> decodeHexBinary(String hexBinary)
          {
            String validHexBinary  = hexBinary.startsWith(("0x")) || hexBinary.startsWith("0X") ? hexBinary.substring(2):hexBinary;
            long mask = Long.parseUnsignedLong(validHexBinary , 16);
            int bitPos = 0;
            Map<Integer, String> decodedModes = new LinkedHashMap<>();
            for(Map.Entry<Integer, String> entry: DER_CONTROL_TYPES.entrySet()){
                int flag = entry.getKey();
                String mode = entry.getValue();
                boolean supported = (mask&flag) != 0;
                if(supported) {
                    decodedModes.put(flag, mode);
                }
                Logger.getLogger(DerUtils.class.getName()).info(String.format("%-6d 0x%08X     %-40s %s%n",
                    bitPos, flag, mode, supported ? " YES" : " NO"));
                bitPos ++;
            }
            return decodedModes;
          }

          private String extractValueHexValue(String xml, String tagName) {
            String openTag = "<" + tagName + ">";
            String closeTag = "</" + tagName + ">";
            int startIndex = xml.indexOf(openTag);
            int endIndex = xml.indexOf(closeTag);
            if (startIndex != -1 && endIndex != -1) {
                return xml.substring(startIndex + openTag.length(), endIndex);
            }
            return "";
          }

          private Map<String, String> extractNestedValues(String xml, String parentTag) {
            Map<String, String> result = new LinkedHashMap<>();
            String parentContent = extractValueHexValue(xml, parentTag);
            if (!parentContent.isEmpty()) {
                result.put("multiplier", extractValueHexValue(parentContent, "multiplier"));
                result.put("value", extractValueHexValue(parentContent, "value"));
            }
            return result;
          }

          public String buildPowerGenerationXml(String derCapabilityXml, String derSettingsXml, Map<String, String> modeValidation) {
            StringBuilder xml = new StringBuilder();
            xml.append("<PowerGeneration xmlns=\"urn:ieee:std:2030.5:ns\">\n");
            
            String modesSupported = extractValueHexValue(derCapabilityXml, "modesSupported");
            xml.append(" <modesSupported>").append(modesSupported).append("</modesSupported>\n");
            
            Map<String, String> rtgMaxVA = extractNestedValues(derCapabilityXml, "rtgMaxVA");
            xml.append(" <rtgMaxVA>\n");
            xml.append(" <multiplier>").append(rtgMaxVA.getOrDefault("multiplier", "0")).append("</multiplier>\n");
            xml.append(" <value>").append(rtgMaxVA.getOrDefault("value", "0")).append("</value>\n");
            xml.append(" </rtgMaxVA>\n");
            
            Map<String, String> rtgMaxW = extractNestedValues(derCapabilityXml, "rtgMaxW");
            xml.append("<rtgMaxW>\n");
            xml.append(" <multiplier>").append(rtgMaxW.getOrDefault("multiplier", "0")).append("</multiplier>\n");
            xml.append(" <value>").append(rtgMaxW.getOrDefault("value", "0")).append("</value>\n");
            xml.append(" </rtgMaxW>\n");
            
            Map<String, String> setMaxW = extractNestedValues(derSettingsXml, "setMaxW");
            xml.append("<setMaxW>\n");
            xml.append(" <multiplier>").append(setMaxW.getOrDefault("multiplier", "0")).append("</multiplier>\n");
            xml.append(" <value>").append(setMaxW.getOrDefault("value", "0")).append("</value>\n");
            xml.append(" </setMaxW>\n");
            
            Map<String, String> setMaxVA = extractNestedValues(derSettingsXml, "setMaxVA");
            xml.append("<setMaxVA>\n");
            xml.append(" <multiplier>").append(setMaxVA.getOrDefault("multiplier", "0")).append("</multiplier>\n");
            xml.append(" <value>").append(setMaxVA.getOrDefault("value", "0")).append("</value>\n");
            xml.append(" </setMaxVA>\n");
            
            xml.append("    <modes>\n");
            if (modeValidation != null && !modeValidation.isEmpty()) {
                for (Map.Entry<String, String> entry : modeValidation.entrySet()) {
                    if ("present".equals(entry.getValue())) {
                        xml.append("     ").append(entry.getKey()).append("\n");
                    }
                }
            }
            xml.append("</modes>\n");
            
            xml.append("</PowerGeneration>");
            return xml.toString();
          }

          public String buildReactivePowerXml(String derCapabilityXml, String derSettingsXml, Map<String, String> modeValidation, Map<String, String> validationChecks) {
            StringBuilder xml = new StringBuilder();
            xml.append("<ReactivePower xmlns=\"urn:ieee:std:2030.5:ns\">\n");
            
            String modesSupported = extractValueHexValue(derCapabilityXml, "modesSupported");
            if (!modesSupported.isEmpty()) {
                xml.append("    <modesSupported>").append(modesSupported).append("</modesSupported>\n");
            }
            
            Map<String, String> rtgMaxVAr = extractNestedValues(derCapabilityXml, "rtgMaxVar");
            Map<String, String> setMaxVAr = extractNestedValues(derSettingsXml, "setMaxVar");
            if (!rtgMaxVAr.isEmpty() && !setMaxVAr.isEmpty()) {
                xml.append("    <rtgMaxVAr>\n");
                xml.append("        <multiplier>").append(rtgMaxVAr.get("multiplier")).append("</multiplier>\n");
                xml.append("        <value>").append(rtgMaxVAr.get("value")).append("</value>\n");
                xml.append("    </rtgMaxVAr>\n");
                xml.append("    <setMaxVAr>\n");
                xml.append("        <multiplier>").append(setMaxVAr.get("multiplier")).append("</multiplier>\n");
                xml.append("        <value>").append(setMaxVAr.get("value")).append("</value>\n");
                xml.append("    </setMaxVAr>\n");
                int rtgVal = Integer.parseInt(rtgMaxVAr.get("value"));
                int setVal = Integer.parseInt(setMaxVAr.get("value"));
                xml.append("    <validation_setMaxVAr_le_rtgMaxVAr>").append(setVal <= rtgVal ? "PASS" : "FAIL").append("</validation_setMaxVAr_le_rtgMaxVAr>\n");
            }
            
            Map<String, String> rtgMaxVArNeg = extractNestedValues(derCapabilityXml, "rtgMaxVarNeg");
            Map<String, String> setMaxVArNeg = extractNestedValues(derSettingsXml, "setMaxVarNeg");
            if (!rtgMaxVArNeg.isEmpty() && !setMaxVArNeg.isEmpty()) {
                xml.append("    <rtgMaxVArNeg>\n");
                xml.append("        <multiplier>").append(rtgMaxVArNeg.get("multiplier")).append("</multiplier>\n");
                xml.append("        <value>").append(rtgMaxVArNeg.get("value")).append("</value>\n");
                xml.append("    </rtgMaxVArNeg>\n");
                xml.append("    <setMaxVArNeg>\n");
                xml.append("        <multiplier>").append(setMaxVArNeg.get("multiplier")).append("</multiplier>\n");
                xml.append("        <value>").append(setMaxVArNeg.get("value")).append("</value>\n");
                xml.append("    </setMaxVArNeg>\n");
                int rtgVal = Integer.parseInt(rtgMaxVArNeg.get("value"));
                int setVal = Integer.parseInt(setMaxVArNeg.get("value"));
                xml.append("    <validation_setMaxVArNeg_ge_rtgMaxVArNeg>").append(setVal >= rtgVal ? "PASS" : "FAIL").append("</validation_setMaxVArNeg_ge_rtgMaxVArNeg>\n");
            }
            
            Map<String, String> setMaxVA = extractNestedValues(derSettingsXml, "setMaxVA");
            if (!setMaxVA.isEmpty()) {
                xml.append("    <setMaxVA>\n");
                xml.append("        <multiplier>").append(setMaxVA.get("multiplier")).append("</multiplier>\n");
                xml.append("        <value>").append(setMaxVA.get("value")).append("</value>\n");
                xml.append("    </setMaxVA>\n");
            }
            
            if (modeValidation != null && !modeValidation.isEmpty()) {
                xml.append("    <modes>\n");
                for (Map.Entry<String, String> entry : modeValidation.entrySet()) {
                    if ("present".equals(entry.getValue())) {
                        xml.append("        ").append(entry.getKey()).append("\n");
                    }
                }
                xml.append("    </modes>\n");
            }
            
            if (validationChecks != null && !validationChecks.isEmpty()) {
                xml.append("    <validationResults>\n");
                for (Map.Entry<String, String> entry : validationChecks.entrySet()) {
                    xml.append("        <").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">\n");
                }
                xml.append("    </validationResults>\n");
            }
            
            xml.append("</ReactivePower>");
            return xml.toString();
          }
    
    public Map<String, Object> reactivePowerHandler(Map<String, String> derReactivePowerTests)
    
    {
        Map<String, Map<String, String>> derReactivePowerValues = new LinkedHashMap<>();
        ArrayList<String> requiredModes = new ArrayList<>();
        requiredModes.add("opModFixedVar");
        try {
            if(derReactivePowerTests.containsKey("derCapability") && derReactivePowerTests.containsKey("derSettings"))
            {
                String xmlderCapability = derReactivePowerTests.get("derCapability");
                String xmlderSettings = derReactivePowerTests.get("derSettings");
                
                Map<String, String> modeValidation = new LinkedHashMap<>();
                if(xmlderCapability.contains("modesSupported"))
                {
                    String modesSupportedHexValue = extractValueHexValue(xmlderCapability, "modesSupported");
                    System.out.println("modesSupported Value: " + modesSupportedHexValue);
                    Map<Integer, String> allDecodedModes = decodeHexBinary(modesSupportedHexValue);
                    
                    for(String requiredMode : requiredModes) {
                        boolean found = allDecodedModes.values().stream().anyMatch(mode -> mode.equals(requiredMode));
                        modeValidation.put(requiredMode, found ? "present" : "absent");
                    }
                 }

                if(xmlderCapability.contains("rtgMaxVar")) {
                    Map<String, String> rtgMaxVAr = extractNestedValues(xmlderCapability, "rtgMaxVar");
                    derReactivePowerValues.put("rtgMaxVAr", rtgMaxVAr);
                }
                if(xmlderCapability.contains("rtgMaxVarNeg")) {
                    Map<String, String> rtgMaxVArNeg = extractNestedValues(xmlderCapability, "rtgMaxVarNeg");
                    derReactivePowerValues.put("rtgMaxVArNeg", rtgMaxVArNeg);
                }
                if(xmlderSettings.contains("setMaxVar")) {
                    Map<String, String> setMaxVAr = extractNestedValues(xmlderSettings, "setMaxVar");
                    derReactivePowerValues.put("setMaxVAr", setMaxVAr);
                }
                if(xmlderSettings.contains("setMaxVarNeg")) {
                    Map<String, String> setMaxVArNeg = extractNestedValues(xmlderSettings, "setMaxVarNeg");
                    derReactivePowerValues.put("setMaxVArNeg", setMaxVArNeg);
                }
                if(xmlderSettings.contains("setMaxVA")) {
                    Map<String, String> setMaxVA = extractNestedValues(xmlderSettings, "setMaxVA");
                    derReactivePowerValues.put("setMaxVA", setMaxVA);
                }
                
                Map<String, String> validationChecks = new LinkedHashMap<>();
                
                boolean hasRtgMaxVAr = xmlderCapability.contains("rtgMaxVar");
                boolean hasSetMaxVAr = xmlderSettings.contains("setMaxVar");
                boolean hasSetMaxVArNeg = xmlderSettings.contains("setMaxVarNeg");
                validationChecks.put("rtgMaxVAr_present", hasRtgMaxVAr ? "pass" : "fail");
                validationChecks.put("setMaxVAr_present", hasSetMaxVAr ? "pass" : "fail");
                validationChecks.put("setMaxVArNeg_present", hasSetMaxVArNeg ? "pass" : "fail");
              
                if (hasSetMaxVAr && hasRtgMaxVAr) {
                    Map<String, String> setMaxVAr = extractNestedValues(xmlderSettings, "setMaxVar");
                    Map<String, String> rtgMaxVAr = extractNestedValues(xmlderCapability, "rtgMaxVar");
                    int setVal = Integer.parseInt(setMaxVAr.get("value"));
                    int rtgVal = Integer.parseInt(rtgMaxVAr.get("value"));
                    validationChecks.put("setMaxVAr_<=_rtgMaxVAr", setVal <= rtgVal ? "pass" : "fail");
                }
                
                if(xmlderCapability.contains("modesSupported")) {
                    String modesSupportedHexValue = extractValueHexValue(xmlderCapability, "modesSupported");
                    Map<Integer, String> allDecodedModes = decodeHexBinary(modesSupportedHexValue);
                    boolean hasVoltVAr = allDecodedModes.values().stream().anyMatch(mode -> mode.equals("opModVoltVar"));
                    validationChecks.put("opModVoltVar_support", hasVoltVAr ? "pass" : "fail");
                }
                
                if (hasSetMaxVArNeg && xmlderCapability.contains("rtgMaxVarNeg")) {
                    Map<String, String> setMaxVArNeg = extractNestedValues(xmlderSettings, "setMaxVarNeg");
                    Map<String, String> rtgMaxVArNeg = extractNestedValues(xmlderCapability, "rtgMaxVarNeg");
                    int setVal = Integer.parseInt(setMaxVArNeg.get("value"));
                    int rtgVal = Integer.parseInt(rtgMaxVArNeg.get("value"));
                    validationChecks.put("setMaxVArNeg_>=_rtgMaxVArNeg", setVal >= rtgVal ? "pass" : "fail");
                }
                
                boolean hasValidRefType = false;
                validationChecks.put("valid_refType", "not_applicable");

                return Map.of("status", "success", "modeValidation", modeValidation, "derReactivePowerValues", derReactivePowerValues, "validationChecks", validationChecks, "xmlOnly", true);
            } else {
                return Map.of("status", "error", "message", "Missing required parameters");
            }
           
           } catch (Exception e) {
               return Map.of("status", "error", "message", e.getMessage());
           }
 





        }
    
}
