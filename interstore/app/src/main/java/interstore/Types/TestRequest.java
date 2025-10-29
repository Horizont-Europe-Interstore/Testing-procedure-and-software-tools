package interstore.Types;

import java.util.Map;

public class TestRequest {
    private String test; 
    private Map<String, Object> object;

    // getters and setters
    public String getTest() { return test; }
    public void setTest(String test) { this.test = test; }

    public Map<String, Object> getObject() { return object; }
    public void setObject(Map<String, Object> object) { this.object = object; }
}
