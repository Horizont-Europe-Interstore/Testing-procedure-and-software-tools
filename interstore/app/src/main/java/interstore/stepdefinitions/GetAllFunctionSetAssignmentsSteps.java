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
public class GetAllFunctionSetAssignmentsSteps {
    private App app; 
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAllFunctionSetAssignmentsSteps.class);
    private Object response;
    private Scenario scenario;
    /*
     *  Given I have a get all function set assignments test setup
    When I execute the get all function set assignments test with subject "<natsSubject>"
    Then the test should complete successfully with all function set assignemnts response containing:
     */
    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }
    
    @Given("^I have a get all function set assignments test setup$") 
    public void i_have_a_get_all_function_set_assignments_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
       
    }

    @When("^I execute the get all function set assignments test with subject \"([^\"]*)\"$")
    public void i_execute_the_get_all_function_set_assignments_test_with_subject(String natsSubject) throws Exception {
        LOGGER.info("Expected response");
        response = app.getAllFsaTest(natsSubject);
        //response = app.getAllEndDevicesTest(natsSubject);
    }

 
    /* check out for the /edev is present in the endev return links , this can be use for validation while creatign the end device 
     * the /edev has to stored in the front end to compare it with the result from the back end 
     */
    @Then("^the test should complete successfully with all function set assignments response containing:$")
    public  void the_test_should_complete_successfully_with_all_function_set_assignments_response_containing(String expectedJson) throws Exception {
        Map<String, String > expectedNoEndDeviceFoundMap = new HashMap<>();
        expectedNoEndDeviceFoundMap.put("message", "No endDevices found."); 
        Map<String, String> defaultexpectedEndDeviceMap = new HashMap<>();
        defaultexpectedEndDeviceMap.put("enddeviceLink", "http://localhost/edev/1");
        ObjectMapper actualObjectMapper = new ObjectMapper();
        Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
        LOGGER.info("the actaul get all functio set assignment response is ....." + actualMap);
        for(Map.Entry<Object, Object> entry:actualMap.entrySet())
        {
            Object key = entry.getKey();
            
            if(key.equals("message") &&  expectedNoEndDeviceFoundMap.containsKey(key))
            {
                scenario.log("actual" + ":" +  actualMap);
                scenario.log("expected" + ":" +  expectedNoEndDeviceFoundMap);
            }
            else if( key.equals("functionSetAssignments"))
            {  
                {
                    scenario.log("actual" + ":" + entry.getValue());
                    scenario.log("expected" + ":" + defaultexpectedEndDeviceMap);
                   } 
               }
            } 
          
        }
        
    }
    