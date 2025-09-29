// package interstore.stepdefinitions;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.When;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.Before;
// import io.cucumber.java.Scenario;
// import interstore.App;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import interstore.ApplicationContextProvider;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import java.util.HashMap;
// import java.util.Map;
// public class CreateFunctionSetAssignmentsSteps {
//     private App app; 
//     private static final Logger LOGGER = LoggerFactory.getLogger(GetAllFunctionSetAssignmentsSteps.class);
//     private Object response;
//     private Scenario scenario;
//     /*
//      *  Given I have a get all function set assignments test setup
//     When I execute the get all function set assignments test with subject "<natsSubject>"
//     Then the test should complete successfully with all function set assignemnts response containing:
//      */
//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }
    
//     @Given("^I have a create function set assignments test setup$") 
//     public void i_have_a_create_function_set_assignments_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
       
//     }

//     @When("^I execute the create function set assignments test with subject \"([^\"]*)\"$")
//     public void i_execute_the_create_function_set_assignments_test_with_subject(String natsSubject) throws Exception {
//         LOGGER.info("Expected response");
//         response = app.createFunctionsetAssignments(natsSubject);
//         LOGGER.info("Actual response: {}", response); 
        
//     }

 
//     /* check out for the /edev is present in the endev return links , this can be use for validation while creatign the end device 
//      * the /edev has to stored in the front end to compare it with the result from the back end 
//      * return Map.of("id", fsaEntity.getId(), "mRID", fsaEntity.getmRID(), "Version", fsaEntity.getVersion());
//      */
//     @Then("^the test should complete successfully with create a function set assignments response containing:$")
//     public void the_test_should_complete_successfully_with_create_a_function_set_assignments_response_containing(String expectedJson) throws Exception  {
//         Map<String, String> expectedMap = new HashMap<>();
//         expectedMap.put("mRID", "1000000");
//         expectedMap.put("Version", "1");
//         expectedMap.put("id", "1");
//         ObjectMapper actualObjectMapper = new ObjectMapper();
//         @SuppressWarnings("unchecked")
//         Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
//         LOGGER.info("Actual response: {}", actualMap);
//         for(Map.Entry<Object, Object> entry:actualMap.entrySet())
//         {  

//             Object key = entry.getKey();
//             if(actualMap.containsKey(key) && expectedMap.containsKey(key))
//             {
//                 LOGGER.info("sample key is:" + entry.getKey());
//                 scenario.log("actual" + ":" + actualMap);
//                 scenario.log("expected" + ":" +  expectedMap);
//             }
//         } 
       
          
//         }
        
//     }
    