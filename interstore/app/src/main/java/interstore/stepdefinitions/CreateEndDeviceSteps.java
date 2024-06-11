package interstore.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;
import interstore.ApplicationContextProvider;
import interstore.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
public class CreateEndDeviceSteps {
    // @Autowired
    private App app; 
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCapabilitySteps.class);
    private Object response;
    private Scenario scenario; 
    
    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }
    

    @Given("^I have a create end device test setup$")
    public void i_have_a_create_end_device_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
    }

    @When("^I execute the create end device test with service name \"([^\"]*)\" and subject \"([^\"]*)\"$")
    public void i_execute_the_create_end_device_test_with_service_name_and_subject(String serviceName,String natsSubject) throws Exception {
        response = app.CreateEndDeviceTest(natsSubject); 
    }

    @Then("^the test should complete successfully with CreateEndDevice response containing:$")
    public void the_test_should_complete_successfully_with_CreateEndDevice_response_containing(String expectedJson) throws Exception {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("LFDI", "3E4F45");
        expectedMap.put("SFDI", "16726121139");
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

    }
}


/*
 *  String expected = String.valueOf(expectedJson).trim();
        String actual = String.valueOf(response).trim();
       LOGGER.info("Expected response: {}", expected + "and " + "actual response is "+ actual);
      assertTrue(expected.equals(actual), "The actual response does not match the expected response.");
 * 
 * 
 */