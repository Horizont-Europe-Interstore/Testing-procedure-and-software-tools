package interstore.XmlValidation;

import java.time.LocalDateTime;

public class XmlValidationResult {
    private String id;
    private String endpoint;
    private String httpMethod;
    private String requestXml;
    private String expectedXml;
    private String actualXml;
    private boolean valid;
    private String differences;
    private LocalDateTime timestamp;

    public XmlValidationResult(String id, String endpoint, String httpMethod, 
                             String requestXml, String expectedXml, String actualXml, 
                             boolean valid, String differences) {
        this.id = id;
        this.endpoint = endpoint;
        this.httpMethod = httpMethod;
        this.requestXml = requestXml;
        this.expectedXml = expectedXml;
        this.actualXml = actualXml;
        this.valid = valid;
        this.differences = differences;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public String getRequestXml() { return requestXml; }
    public void setRequestXml(String requestXml) { this.requestXml = requestXml; }

    public String getExpectedXml() { return expectedXml; }
    public void setExpectedXml(String expectedXml) { this.expectedXml = expectedXml; }

    public String getActualXml() { return actualXml; }
    public void setActualXml(String actualXml) { this.actualXml = actualXml; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getDifferences() { return differences; }
    public void setDifferences(String differences) { this.differences = differences; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}