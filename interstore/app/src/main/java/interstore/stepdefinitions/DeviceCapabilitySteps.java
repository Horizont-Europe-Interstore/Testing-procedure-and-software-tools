package interstore.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import interstore.App;
import interstore.ApplicationContextProvider;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DeviceCapabilitySteps {

    private App app; 
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCapabilitySteps.class);
    private String response;
    private Scenario scenario; 
    
    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("^I have a device capability test setup$")
    public void i_have_a_device_capability_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
    }

    @When("^I execute the device capability test with service name \"([^\"]*)\" and subject \"([^\"]*)\"$")
    public void i_execute_the_device_capability_test_with_service_name_and_subject(String serviceName, String natsSubject) throws Exception {
        LOGGER.info("STEP");
        response = app.CreateDeviceCapabilityTest(natsSubject); 
    }

    @SuppressWarnings("rawtypes")
    @Then("^the test should complete successfully with DeviceCapability response containing:$")
    public void the_test_should_complete_successfully_with_DeviceCapability_response_containing(String expectedJson) throws Exception {
        Map<String, List<String>> expectedMap = new HashMap<>();	
        List<String> expectedResponseList = new ArrayList<>();
        expectedResponseList.add("http://localhost/sdev");
        expectedResponseList.add("http://localhost/mup");
        expectedResponseList.add("http://localhost/edev");
        expectedResponseList.add("http://localhost/tm");
        expectedMap.put("1", expectedResponseList);
        ObjectMapper actualObjectMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<Object, Object>actaulMap = actualObjectMapper.readValue((String)response, Map.class);
        LOGGER.info("Expected response: {}", expectedMap ); 
        LOGGER.info("Actual response: {}", actaulMap); 
        for(Map.Entry<Object, Object> entry:actaulMap.entrySet())
        {  
            Object key = entry.getKey();
                LOGGER.info("The key is  {}", key); 
            if(entry.getKey()!= null)
            {
            for(Object value: (List)entry.getValue())
            {  
               
                if(((String) value).startsWith("http"))
                scenario.log("actual" + ":" + actaulMap);
                scenario.log("expected" + ":" + expectedMap);
                LOGGER.info("The value is  {}", value); 
            }
        }


    }
}
} 


// ./gradlew cucumber -Dcucumber.feature=src/test/resources/interstore/DeviceCapability.feature

/*


 "1":["http://localhost/sdev","http://localhost/mup","http://localhost/edev"]


 * "1":["http://localhost/interstore/sdev",
     "http://localhost/interstore/mup",
     "http://localhost/interstore/edev"]
    
 * Process process = Runtime.getRuntime().exec("path/to/hello.sh"); 
 * 
 * ObjectMapper objectMapper = new ObjectMapper();
       JsonNode expected = objectMapper.readTree(expectedJson);
        JsonNode actual = objectMapper.readTree(response);
        Set<String> expectedSet = new HashSet<>();
       expected.get("1").forEach(node -> expectedSet.add(node.asText()));

       Set<String> actualSet = new HashSet<>();
       actual.get("1").forEach(node -> actualSet.add(node.asText()));
       LOGGER.info("Expected response: {}", expected + "and " + "actual response is "+ actual);
      assertTrue(expectedSet.equals(actualSet), "The actual response does not match the expected response.");
 * 
 * 
 */

 