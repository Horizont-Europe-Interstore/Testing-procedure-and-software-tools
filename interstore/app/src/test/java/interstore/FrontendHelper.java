package interstore;

import java.util.Map;

public class FrontendHelper {
    
    public static void sendResult(String expectedXml, String actualXml, Map<String, String> testResult) {
        // Send test results to frontend
        System.out.println("Test Result: " + testResult.get("status"));
        System.out.println("Reason: " + testResult.get("reason"));
        System.out.println("Expected XML: " + expectedXml);
        System.out.println("Actual XML: " + actualXml);
    }
}