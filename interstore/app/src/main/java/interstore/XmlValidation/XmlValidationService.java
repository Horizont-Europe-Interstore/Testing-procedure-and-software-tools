package interstore.XmlValidation;

import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.annotation.PostConstruct;

@Service
public class XmlValidationService {
    
    private final Map<String, XmlValidationResult> validationResults = new ConcurrentHashMap<>();
    private final Map<String, String> expectedXmlCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void preloadExpectedXml() {
        // Preload common expected XML files at startup
        String[] commonFiles = {
            "_dcap_get.xml", "_dcap_post.xml", "_dcap_put.xml",
            "_edev_get.xml", "_edev_post.xml", "_edev_put.xml",
            "_der_get.xml", "_der_post.xml", "_der_put.xml",
            "_fsa_get.xml", "_fsa_post.xml", "_fsa_put.xml"
        };
        
        for (String fileName : commonFiles) {
            try {
                ClassPathResource resource = new ClassPathResource("expected-xml/" + fileName);
                if (resource.exists()) {
                    String content = resource.getContentAsString(StandardCharsets.UTF_8);
                    expectedXmlCache.put(fileName, content);
                }
            } catch (IOException ignored) {}
        }
    }

    public XmlValidationResult validateXml(String endpoint, String httpMethod, 
                                         String requestXml, String actualXml) {
        String id = UUID.randomUUID().toString();
        
        try {
            String expectedXml = loadExpectedXml(endpoint, httpMethod);
            
            Diff diff = DiffBuilder.compare(expectedXml)
                    .withTest(actualXml)
                    .ignoreWhitespace()
                    .build();
            
            boolean isValid = !diff.hasDifferences();
            String differences = formatDifferences(diff);
            
            XmlValidationResult result = new XmlValidationResult(
                id, endpoint, httpMethod, requestXml, expectedXml, 
                actualXml, isValid, differences
            );
            
            validationResults.put(id, result);
            return result;
            
        } catch (Exception e) {
            XmlValidationResult errorResult = new XmlValidationResult(
                id, endpoint, httpMethod, requestXml, "Error loading expected XML", 
                actualXml, false, "Error: " + e.getMessage()
            );
            validationResults.put(id, errorResult);
            return errorResult;
        }
    }

    private String loadExpectedXml(String endpoint, String httpMethod) throws IOException {
        String fileName = sanitizeFileName(endpoint) + "_" + httpMethod.toLowerCase() + ".xml";
        
        // Check cache first
        String cachedXml = expectedXmlCache.get(fileName);
        if (cachedXml != null) {
            return cachedXml;
        }
        
        // Load from classpath and cache
        ClassPathResource resource = new ClassPathResource("expected-xml/" + fileName);
        if (!resource.exists()) {
            throw new IOException("Expected XML file not found: " + fileName);
        }
        
        String content = resource.getContentAsString(StandardCharsets.UTF_8);
        expectedXmlCache.put(fileName, content);
        return content;
    }

    private String sanitizeFileName(String endpoint) {
        return endpoint.replaceAll("[^a-zA-Z0-9]", "_");
    }

    private String formatDifferences(Diff diff) {
        if (!diff.hasDifferences()) {
            return "No differences found";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Difference difference : diff.getDifferences()) {
            sb.append("- ").append(difference.toString()).append("\n");
        }
        return sb.toString();
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