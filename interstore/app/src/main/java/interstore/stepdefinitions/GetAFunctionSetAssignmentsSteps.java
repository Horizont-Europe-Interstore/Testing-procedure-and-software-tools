// package interstore.stepdefinitions;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.When;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.Before;
// import io.cucumber.java.Scenario;
// import interstore.App;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import interstore.ApplicationContextProvider;
// import java.util.HashSet;
// import java.util.Set;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import java.util.HashMap;
// import java.util.Map;

// public class GetAFunctionSetAssignmentsSteps {
//     private App app; 
//     private static final Logger LOGGER = LoggerFactory.getLogger(GetAllFunctionSetAssignmentsSteps.class);
//     private Object response;
//     private Scenario scenario;

//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }

//     @Given("^I have a get a function set assignments test setup$") 
//     public void i_have_a_get_all_function_set_assignments_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
       
//     }

//     @When("^I execute the get a function set assignments test with subject \"([^\"]*)\"$")
//     public void i_execute_the_get_a_function_set_assignments_test_with_subject(String natsSubject) throws Exception {
//         LOGGER.info("Expected response");
//         response = app.getAFunctionSetAssignments(natsSubject);
//         LOGGER.info("Actual response: {}", response); 
       
//     }

//     @Then("^the test should complete successfully with a function set assignments response containing:$")
//     public  void the_test_should_complete_successfully_with_a_function_set_assignments_response_containing(String expectedJson) throws Exception {
//         Map<String, String> expectedMap = new HashMap<>();
//         Set<String> includeKeys = new HashSet<>(); 
//         includeKeys.add("dERProgramListLink");
//         includeKeys.add("demandResponseProgramListLink");
//         includeKeys.add("fileListLink");
//         includeKeys.add("traiffProfileListLink");
//         includeKeys.add("messagingProgramListLink");
//         includeKeys.add("usagePointListLink");
//         includeKeys.add("customerAccountListLink");
//         includeKeys.add("prepaymentListLink");
//         includeKeys.add("responseSetListLink");
//         expectedMap.put("mRID", "A1000000");
//         expectedMap.put("description", "der program fsa");
//         expectedMap.put("deviceCategory", "1");
//         expectedMap.put("id", "1");
//         expectedMap.put("dERProgramListLink", "http://localhost/der");
//         expectedMap.put("subscribable", "1");
//         expectedMap.put("version", "1");
//         expectedMap.put("functionSetAssignmentsLink", "http://localhost/edev/1/fsa/1");
//        ObjectMapper actualObjectMapper = new ObjectMapper(); 
//       @SuppressWarnings("unchecked")
//     Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
//       LOGGER.info("Actual response before comparing with expected : {}", actualMap);
//       @SuppressWarnings("unchecked")
//     Map<String, Object> fsaMap = (Map<String, Object>) actualMap.get("FunctionSetAssignments");
//     try {
        
//       for(Map.Entry<String, String> entry:expectedMap.entrySet())
//       {  
//           String key = entry.getKey(); 
//           if( fsaMap.containsKey(key) || expectedMap.containsKey(key) || includeKeys.contains(key))
//           {
//               LOGGER.info("sample key is:" + entry.getKey());
//               scenario.log("actual" + ":" + actualMap);
//               scenario.log("expected" + ":" +  expectedMap);
//           }
//       } 
//     } catch (Exception e) {
//         LOGGER.error("Error comparing actual and expected responses", e);
//         throw e;
//     } 

//     }
// }

// /*
//  * Response message giving back to test class  : {"FunctionSetAssignments":{"mRID":"A1000000","description":"der program fsa","id":1,"dERProgramListLink":"http://localhost/der","subscribable":1,"version":1,"functionSetAssignmentsLink":"http://localhost/edev/1/fsa/1"}}
//  * 
//  * 
//  */