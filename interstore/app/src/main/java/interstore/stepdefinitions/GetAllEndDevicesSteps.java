package interstore.stepdefinitions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import interstore.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import interstore.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
public class GetAllEndDevicesSteps {
    private App app; 
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAllEndDevicesSteps.class);
    private Object response;
    private Scenario scenario;

    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }
    
    @Given("^I have a device capability and end device test setup$")
    public void i_have_a_device_capability_and_end_device_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
       
    }

    @When("^I execute the end device test with subject \"([^\"]*)\"$")
    public void i_execute_the_end_device_test_with_subject(String natsSubject) throws Exception {
        LOGGER.info("Expected response");
        response = app.getAllEndDevicesTest(natsSubject);
    }



    @Then("^the test should complete successfully with EndDevice response containing:$")
    public  void then_the_test_should_complete_successfully_with_EndDevice_response_containing(String expectedJson) throws Exception {
        Map<String, String > expectedNoEndDeviceFoundMap = new HashMap<>();
        expectedNoEndDeviceFoundMap.put("message", "No endDevices found."); 
        Map<String, String> defaultexpectedEndDeviceMap = new HashMap<>();
        defaultexpectedEndDeviceMap.put("id", "1");
        defaultexpectedEndDeviceMap.put("sfdi", "16726121139");
        defaultexpectedEndDeviceMap.put("deviceCategory", "1");
        defaultexpectedEndDeviceMap.put("registrationLink", "/rg");
        defaultexpectedEndDeviceMap.put("endDeviceLink", "/edev/1");
        defaultexpectedEndDeviceMap.put("functionSetAssignmentsListLink", "/fsa");
        defaultexpectedEndDeviceMap.put("subscriptionListLink", "/sub");
        defaultexpectedEndDeviceMap.put("derlistLink", "/der");
        defaultexpectedEndDeviceMap.put("deviceCategory", "1");
        defaultexpectedEndDeviceMap.put("hexBinary160", "3E4F45");
        defaultexpectedEndDeviceMap.put("deviceStatusLink", "/dstat"); 
        LOGGER.info("Expected No Device Message : {} ", expectedNoEndDeviceFoundMap); 
        LOGGER.info("Expected End Devices present response : {} ", defaultexpectedEndDeviceMap);
        ObjectMapper actualObjectMapper = new ObjectMapper();
        Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
        LOGGER.info("the actaul  response is ....." + actualMap);
        for(Map.Entry<Object, Object> entry:actualMap.entrySet())
        {
            Object key = entry.getKey();
            
            if(key.equals("message") &&  expectedNoEndDeviceFoundMap.containsKey(key))
            {
                scenario.log("actual" + ":" +  actualMap);
                scenario.log("expected" + ":" +  expectedNoEndDeviceFoundMap);
            }
            else if( key.equals("endDevices"))
            {  
                {
                    scenario.log("actual" + ":" + entry.getValue());
                    scenario.log("expected" + ":" + defaultexpectedEndDeviceMap);
                   } 
               }
            } 
          
        }
        
    }
    




/*    
 [{id=1, deviceCategory=1, hexBinary160=3E4F45, endDeviceLink=http://localhost/edev/1, 
    deviceStatusLink=http://localhost/edev/1/dstat, registrationLink=http://localhost/edev/1/rg,
     functionSetAssignmentsListLink=http://localhost/edev/1/fsa, derlistLink=http://localhost/edev/1, 
     subscriptionListLink=http://localhost/edev/1/sub, sfdi=16726121139}]
 *     // String[] expectedresponse = expectedJson.split("\\n\\n");
 * 
 *   expectedResponseList.add("{\"1\": \"http://localhost/interstore/edev/1\"}");
        expectedResponseList.add("{\"2\": \"http://localhost/interstore/edev/2\"}");
 LOGGER.info("the values are " + entry.getValue()); 
               List<Map<String, String>>validateResponse = (List<Map<String, String>>) entry.getValue();
               for(Map.Entry<String, String> endDevice:((Map<String, String>) validateResponse).entrySet())
              
             
                  //if(((Map<String, String>) validateResponse).keySet().equals(defaultexpectedEndDeviceMap.keySet()))


 */