package interstore.TestResults;

import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = {"http://localhost:3001", "https://172.31.47.67"})
public class TestResult {
    
    private static final Logger LOGGER = Logger.getLogger(TestResult.class.getName());
    private static final AtomicReference<String> latestTestResult = new AtomicReference<>(null);
    
    public TestResult() {
        LOGGER.info("🚀 TestResult Controller INITIALIZED by Spring Boot!");
        LOGGER.info("🔗 Endpoints registered: /test-results and /");
    }
    
    public static void storeTestResult(String testResultJson, String actualXml, String expectedXml) {
        try {
            // Parse the JSON to extract fields and add XML content
            String jsonWithoutClosingBrace = testResultJson.substring(0, testResultJson.lastIndexOf('}'));
            
            // Escape XML for JSON
            String escapedActualXml = actualXml.replace("\\", "\\\\")
                                               .replace("\"", "\\\"")
                                               .replace("\n", "\\n")
                                               .replace("\r", "\\r")
                                               .replace("\t", "\\t");
                                               
            String escapedExpectedXml = expectedXml.replace("\\", "\\\\")
                                                   .replace("\"", "\\\"")
                                                   .replace("\n", "\\n")
                                                   .replace("\r", "\\r")
                                                   .replace("\t", "\\t");
            
            // Create complete JSON with XML fields added
            String completeResult = jsonWithoutClosingBrace + 
                                  ",\"actualXml\":\"" + escapedActualXml + "\"" +
                                  ",\"expectedXml\":\"" + escapedExpectedXml + "\"}";
            
            latestTestResult.set(completeResult);
            LOGGER.info("📋 Test result stored with XML data");
            LOGGER.info("📏 Actual XML length: " + actualXml.length() + " chars");
            LOGGER.info("📏 Expected XML length: " + expectedXml.length() + " chars");
        } catch (Exception e) {
            LOGGER.severe("Failed to store test result: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @GetMapping("/test-results")
    public String getLatestTestResult() {
        LOGGER.info("🔴 REQUEST HIT: /test-results endpoint called!");
        LOGGER.info("🔍 Request received at: " + java.time.Instant.now());
        
        String result = latestTestResult.get();
        LOGGER.info("📤 Serving test result to React: " + (result != null ? "Available" : "None"));
        
        if (result != null) {
            LOGGER.info("📝 Response length: " + result.length() + " characters");
        }
        
        return result != null ? result : "{}";
    }
    
    @GetMapping("/")
    public String home() {
        LOGGER.info("🏠 REQUEST HIT: Home endpoint (/) called!");
        return "<h1>IEEE 2030.5 Test Server</h1>" +
               "<p>Available endpoints:</p>" +
               "<ul>" +
               "<li><a href='/test-results'>GET /test-results</a> - Get test results</li>" +
               "</ul>" +
               "<p>Latest result available: " + (latestTestResult.get() != null) + "</p>";
    }
}