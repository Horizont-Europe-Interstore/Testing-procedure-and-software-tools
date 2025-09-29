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
// public class GetRegisteredEndDeviceSteps {
//     private App app; 
//     private static final Logger LOGGER = LoggerFactory.getLogger(GetRegisteredEndDeviceSteps.class);
//     private Object response;
//     private Scenario scenario; 
//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }
    

//     @Given("^I have to find registered an end device test setup$")
//     public void i_have_to_find_registered_an_end_device_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
//     }

//     @When("^I execute the find registered end device test with service name \"([^\"]*)\" and subject \"([^\"]*)\"$")
//     public void i_execute_the_find_registered_end_device_test_with_service_name_and_subject(String serviceName, String natsSubject) throws Exception {
//         response = app.findRegisterdEndDeviceTest(natsSubject);
//     }

//     @Then("^the test should complete successfully with find RegisterEndDevice response containing:$")
//     public void the_test_should_complete_successfully_with_find_RegisterEndDevice_response_containing(String expectedJson) throws Exception {
//          Map<String, Object> expectedMap = new HashMap<>();
//          expectedMap.put("pin", 111112L);
//          expectedMap.put("id", 1);
//          expectedMap.put("linkRgid", "http://localhost/edev/1/rg/1");
//          expectedMap.put("dateTimeRegistered", "null"); 
//         LOGGER.info("the actual response  is:" + response);
//         ObjectMapper actualObjectMapper = new ObjectMapper(); 
//         Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
//        LOGGER.info("Actual response before comparing with expected : {}", actualMap);
//        Map<String, Object> registeredEndDeviceMap = (Map<String, Object>) actualMap.get("RegisteredEndDevice");
//        for(Map.Entry<String, Object> entry:expectedMap.entrySet())
//        {  
          
//            //Object key = entry.getKey();
//            String key = entry.getKey(); 
//            if(registeredEndDeviceMap.containsKey(key) && expectedMap.containsKey(key))
//            {
//                LOGGER.info("sample key is:" + entry.getKey());
//                scenario.log("actual" + ":" + registeredEndDeviceMap);
//                scenario.log("expected" + ":" +  expectedMap);
//            }
//        }  
    

//     }
    
// }
