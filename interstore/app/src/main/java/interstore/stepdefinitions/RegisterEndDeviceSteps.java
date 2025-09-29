// package interstore.stepdefinitions;
// import interstore.App;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.When;
// import io.cucumber.java.en.Then;
// import static org.junit.jupiter.api.Assertions.*;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Map.Entry;

// import io.cucumber.java.Before;
// import io.cucumber.java.Scenario;
// import interstore.ApplicationContextProvider;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import com.fasterxml.jackson.databind.ObjectMapper;
// public class RegisterEndDeviceSteps {
//     private App app; 
//     private static final Logger LOGGER = LoggerFactory.getLogger(RegisterEndDeviceSteps.class);
//     private Object response;
//     private Scenario scenario; 
//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }
    

//     @Given("^I have a register an end device test setup$")
//     public void i_have_a_register_an_end_device_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
//     }

//     @When("^I execute the register end device test with service name \"([^\"]*)\" and subject \"([^\"]*)\"$")
//     public void i_execute_the_register_end_device_test_with_service_name_and_subject(String serviceName, String natsSubject) throws Exception {
//         response = app.createEndDeviceRegistrationTest(natsSubject);
//     }

//     @Then("^the test should complete successfully with RegisterEndDevice response containing:$")
//     public void the_test_should_complete_successfully_with_RegisterEndDevice_response_containing(String expectedJson) throws Exception {
//          Map<String, Long> expectedMap = new HashMap<>();
//          expectedMap.put("pin", 111112L);
        
//         LOGGER.info("the actual response  is:" + response);
//        // ObjectMapper actualObjectMapper = new ObjectMapper(); 
//        //Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
//        //Map<String, Object> endDeviceMap = (Map<String, Object>) actualMap.get("endDevice");
//        for(Entry<String, Long> entry:expectedMap.entrySet())
//        {  
//            String key = entry.getKey(); 
//            if(((Map<String, Long>) response).containsKey(key) && expectedMap.containsKey(key))
//            {
//                LOGGER.info("sample key is:" + entry.getKey());
//                scenario.log("actual" + ":" + response);
//                scenario.log("expected" + ":" +  expectedMap);
//            }
//        }  
    

//     }
// }
