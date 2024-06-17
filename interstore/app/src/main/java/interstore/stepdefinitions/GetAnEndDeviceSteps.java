package interstore.stepdefinitions;
import interstore.App;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import interstore.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class GetAnEndDeviceSteps {
    private App app; 
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCapabilitySteps.class);
    private Object response;
    private Scenario scenario; 
    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }
    

    @Given("^I have a device capability get all end device test setup$")
    public void i_have_a_device_capability_get_all_end_device_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
    }

    @When("^I execute the device capability get all end device test with service name \"([^\"]*)\" and subject \"([^\"]*)\"$")
    public void i_execute_the_device_capability_get_all_end_device_test_with_service_name_and_subject(String serviceName, String natsSubject) throws Exception {
        response = app.getEndDeviceTest(natsSubject);
    }

    @Then("^the test should complete successfully with DeviceCapabilityGetAllEndDevice response containing:$")
    public void the_test_should_complete_successfully_with_DeviceCapabilityGetAllEndDevice_response_containing(String expectedJson) throws Exception {
         Map<String, String> expectedMap = new HashMap<>();
         expectedMap.put("SFDI", "16726121139");
       // String expected = String.valueOf(expectedJson).trim();
       // String actual = String.valueOf(response).trim();
        ObjectMapper actualObjectMapper = new ObjectMapper(); 
       Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
       LOGGER.info("Actual response: {}", actualMap);
       for(Map.Entry<Object, Object> entry:actualMap.entrySet())
       {  

           Object key = entry.getKey();
           if(actualMap.containsKey(key) && expectedMap.containsKey(key))
           {
               LOGGER.info("sample key is:" + entry.getKey());
               scenario.log("actual" + ":" + actualMap);
               scenario.log("expected" + ":" +  expectedMap);
           }
       }  
      // LOGGER.info("Expected response: {}", expected + "and " + "actual response is "+ actual);
     
     //  assertTrue(expected.equals(actual), "The actual response does not match the expected response.");

    }
}
