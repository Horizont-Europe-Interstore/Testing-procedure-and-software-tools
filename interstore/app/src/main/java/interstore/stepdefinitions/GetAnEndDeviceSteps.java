package interstore.stepdefinitions;

import interstore.App;
import interstore.ApplicationContextProvider;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
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

import static org.junit.jupiter.api.Assertions.assertTrue;


public class GetAnEndDeviceSteps {
    private App app; 
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAnEndDeviceSteps.class);
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
         expectedMap.put("sfdi", "16726121139");
         expectedMap.put("id", "1");
         expectedMap.put("deviceCategory", "1");
         expectedMap.put("hexBinary160", "3E4F45");
         expectedMap.put("endDeviceLink", "http://localhost/edev/1");
         expectedMap.put("deviceStatusLink", "http://localhost/edev/1/dstat");
         expectedMap.put("registrationLink", "http://localhost/edev/1/rg");
         expectedMap.put("functionSetAssignmentsListLink", "http://localhost/edev/1/fsa");
         expectedMap.put("derlistLink", "http://localhost/edev/1defaultLink");
         expectedMap.put("subscriptionListLink", "http://localhost/edev/1/sub");
        ObjectMapper actualObjectMapper = new ObjectMapper(); 
       Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
       LOGGER.info("Actual response before comparing with expected : {}", actualMap);
       Map<String, Object> endDeviceMap = (Map<String, Object>) actualMap.get("endDevice");
       for(Map.Entry<String, String> entry:expectedMap.entrySet())
       {  
          
           //Object key = entry.getKey();
           String key = entry.getKey(); 
           if(endDeviceMap.containsKey(key) && expectedMap.containsKey(key))
           {
               LOGGER.info("sample key is:" + entry.getKey());
               scenario.log("actual" + ":" + actualMap);
               scenario.log("expected" + ":" +  expectedMap);
           }
       }  
    

    }
}

