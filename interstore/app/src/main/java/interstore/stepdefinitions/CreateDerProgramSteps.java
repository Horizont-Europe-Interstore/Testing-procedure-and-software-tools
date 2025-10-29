// package interstore.stepdefinitions;

// import java.util.HashMap;
// import java.util.Map;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import interstore.App;
// import interstore.ApplicationContextProvider;
// import io.cucumber.java.Before;
// import io.cucumber.java.Scenario;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;

// public class CreateDerProgramSteps {
//     private App app; 
//     private static final Logger LOGGER = LoggerFactory.getLogger(CreateDerProgramSteps.class);
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
    
//     @Given("^I have a create der program test setup$") 
//     public void i_have_a_create_der_program_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
       
//     }

//     @When("^I execute the create der program test with subject \"([^\"]*)\"$")
//     public void i_execute_the_create_der_program_test_with_subject(String natsSubject) throws Exception {
//          response = app.createDerProgram(natsSubject);
//         LOGGER.info("Actual response: {}", response); 
        
//     }

 
//     /* check out for the /edev is present in the endev return links , this can be use for validation while creatign the end device 
//      * the /edev has to stored in the front end to compare it with the result from the back end 
//      * return Map.of("id", derProgramEntity.getId(), "CurrentRMS", derEntity.getRtgMaxA(), "AmpereHour", derEntity.getRtgMaxAh())   
//      */
//     @Then("^the test should complete successfully with create a der program response containing:$")
//     public void the_test_should_complete_successfully_with_create_a_der_program_response_containing(String expectedJson) throws Exception  {
//         Map<String, String> expectedMap = new HashMap<>();
//         expectedMap.put("id", "1");
//         expectedMap.put("mRID", "B01000000");
//         expectedMap.put("primacy", "89"); 
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
        
    
// }
