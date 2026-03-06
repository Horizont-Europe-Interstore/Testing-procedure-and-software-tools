package interstore.DER;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class DerPowerTests {
    private static final Logger LOGGER = Logger.getLogger(DerPowerTests.class.getName());

    public Map<String, Object> powerGenerationTest(Map<String, String> derPowerTests) {
        try {
            String derCapabilityXml = derPowerTests.get("derCapability");
            String derSettingsXml = derPowerTests.get("derSettings");

            Document capDoc = parseXml(derCapabilityXml);
            Document setDoc = parseXml(derSettingsXml);

            List<Map<String, Object>> testResults = new ArrayList<>();
            boolean allPassed = true;

            // Test 1: modesSupported includes opModMaxLimW (bit 20)
            String modesSupported = getElementValue(capDoc, "modesSupported");
            if (modesSupported != null) {
                long modes = parseHexValue(modesSupported);
                boolean hasOpModMaxLimW = (modes & (1L << 20)) != 0;
                testResults.add(Map.of(
                    "test", "opModMaxLimW Support",
                    "expected", "Bit 20 set in modesSupported",
                    "actual", "modesSupported=" + modesSupported + " (bit 20: " + hasOpModMaxLimW + ")",
                    "status", hasOpModMaxLimW ? "PASS" : "FAIL"
                ));
                allPassed &= hasOpModMaxLimW;
            }

            // Test 2: rtgMaxW exists
            String rtgMaxW = getElementValue(capDoc, "rtgMaxW");
            boolean hasRtgMaxW = rtgMaxW != null && !rtgMaxW.isEmpty();
            testResults.add(Map.of(
                "test", "DERCapability rtgMaxW",
                "expected", "rtgMaxW must be present",
                "actual", hasRtgMaxW ? "rtgMaxW=" + rtgMaxW : "rtgMaxW not found",
                "status", hasRtgMaxW ? "PASS" : "FAIL"
            ));
            allPassed &= hasRtgMaxW;

            // Test 3: If power adjustable, check setMaxW
            String setMaxW = getElementValue(setDoc, "setMaxW");
            if (hasRtgMaxW) {
                boolean hasSetMaxW = setMaxW != null && !setMaxW.isEmpty();
                testResults.add(Map.of(
                    "test", "DERSettings setMaxW",
                    "expected", "setMaxW must be present for power adjustment",
                    "actual", hasSetMaxW ? "setMaxW=" + setMaxW : "setMaxW not found",
                    "status", hasSetMaxW ? "PASS" : "FAIL"
                ));
                allPassed &= hasSetMaxW;

                // Test: setMaxW <= rtgMaxW
                if (hasSetMaxW) {
                    try {
                        LOGGER.info("Parsing rtgMaxW: '" + rtgMaxW + "'");
                        LOGGER.info("Parsing setMaxW: '" + setMaxW + "'");
                        long rtgMax = Long.parseLong(rtgMaxW.replaceAll("\\s+", ""));
                        long setMax = Long.parseLong(setMaxW.replaceAll("\\s+", ""));
                        boolean validSetMaxW = setMax <= rtgMax;
                        testResults.add(Map.of(
                            "test", "Nameplate Rating Validation (W)",
                            "expected", "setMaxW <= rtgMaxW",
                            "actual", "setMaxW=" + setMax + ", rtgMaxW=" + rtgMax + " (" + (validSetMaxW ? "valid" : "invalid") + ")",
                            "status", validSetMaxW ? "PASS" : "FAIL"
                        ));
                        allPassed &= validSetMaxW;
                    } catch (NumberFormatException e) {
                        LOGGER.severe("Failed to parse W values: " + e.getMessage());
                        testResults.add(Map.of(
                            "test", "Nameplate Rating Validation (W)",
                            "expected", "Valid numeric values",
                            "actual", "Parse error: " + e.getMessage(),
                            "status", "FAIL"
                        ));
                        allPassed = false;
                    }
                }
            }

            // Test 4: rtgMaxVA and setMaxVA
            String rtgMaxVA = getElementValue(capDoc, "rtgMaxVA");
            String setMaxVA = getElementValue(setDoc, "setMaxVA");
            if (rtgMaxVA != null && !rtgMaxVA.isEmpty()) {
                boolean hasSetMaxVA = setMaxVA != null && !setMaxVA.isEmpty();
                testResults.add(Map.of(
                    "test", "DERCapability rtgMaxVA & DERSettings setMaxVA",
                    "expected", "Both rtgMaxVA and setMaxVA must be present",
                    "actual", "rtgMaxVA=" + rtgMaxVA + ", setMaxVA=" + (hasSetMaxVA ? setMaxVA : "not found"),
                    "status", hasSetMaxVA ? "PASS" : "FAIL"
                ));
                allPassed &= hasSetMaxVA;

                // Test: setMaxVA <= rtgMaxVA
                if (hasSetMaxVA) {
                    try {
                        long rtgMax = Long.parseLong(rtgMaxVA.replaceAll("\\s+", ""));
                        long setMax = Long.parseLong(setMaxVA.replaceAll("\\s+", ""));
                        boolean validSetMaxVA = setMax <= rtgMax;
                        testResults.add(Map.of(
                            "test", "Nameplate Rating Validation (VA)",
                            "expected", "setMaxVA <= rtgMaxVA",
                            "actual", "setMaxVA=" + setMax + ", rtgMaxVA=" + rtgMax + " (" + (validSetMaxVA ? "valid" : "invalid") + ")",
                            "status", validSetMaxVA ? "PASS" : "FAIL"
                        ));
                        allPassed &= validSetMaxVA;
                    } catch (NumberFormatException e) {
                        LOGGER.severe("Failed to parse VA values: " + e.getMessage());
                        testResults.add(Map.of(
                            "test", "Nameplate Rating Validation (VA)",
                            "expected", "Valid numeric values",
                            "actual", "Parse error: " + e.getMessage(),
                            "status", "FAIL"
                        ));
                        allPassed = false;
                    }
                }
            }

            // Test 5: opModVoltWatt support
            if (modesSupported != null) {
                long modes = parseHexValue(modesSupported);
                boolean hasOpModVoltWatt = (modes & (1L << 12)) != 0;
                testResults.add(Map.of(
                    "test", "opModVoltWatt Curve Support",
                    "expected", "Bit 12 set in modesSupported",
                    "actual", "modesSupported=" + modesSupported + " (bit 12: " + hasOpModVoltWatt + ")",
                    "status", hasOpModVoltWatt ? "PASS" : "FAIL"
                ));
                allPassed &= hasOpModVoltWatt;

                // Test 5b: opModFixedVAr or opModFixedPF
                boolean hasOpModFixedVAr = (modes & (1L << 8)) != 0;
                boolean hasOpModFixedPF = (modes & (1L << 9)) != 0;
                boolean hasEither = hasOpModFixedVAr || hasOpModFixedPF;
                testResults.add(Map.of(
                    "test", "opModFixedVAr or opModFixedPF Support",
                    "expected", "Bit 8 (FixedVAr) or Bit 9 (FixedPF) set",
                    "actual", "bit 8: " + hasOpModFixedVAr + ", bit 9: " + hasOpModFixedPF,
                    "status", hasEither ? "PASS" : "FAIL"
                ));
            }

            return Map.of(
                "End result", allPassed ? "stored" : "failed",
                "Steps", List.of(Map.of(
                    "Name", "Power Generation Test",
                    "Value", "Overall Status: " + (allPassed ? "PASS" : "FAIL"),
                    "TestResults", testResults,
                    "DERCapabilityXML", derCapabilityXml,
                    "DERSettingsXML", derSettingsXml
                ))
            );

        } catch (Exception e) {
            LOGGER.severe("Power Generation Test failed: " + e.getMessage());
            return Map.of(
                "End result", "failed",
                "Steps", List.of(Map.of(
                    "Name", "Power Generation Test",
                    "Value", "ERROR: " + e.getMessage()
                ))
            );
        }
    }

    private Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(
            new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    private String getElementValue(Document doc, String tagName) {
        NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return null;
    }

    private long parseHexValue(String hexValue) {
        String cleaned = hexValue.trim().replaceAll("^0x", "");
        return Long.parseLong(cleaned, 16);
    }
}


