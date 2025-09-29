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

// public class GetADerProgramSteps {
//     private App app; 
//     private static final Logger LOGGER = LoggerFactory.getLogger(GetADerProgramSteps.class);
//     private Object response;
//     private Scenario scenario;
   
//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }
    
//       @Given("^I have a get a der program test setup$") 
//     public void i_have_a_get_a_der_program_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
       
//     }

//     @When("^I execute the get a der program test with subject \"([^\"]*)\"$")
//     public void i_execute_the_get_a_der_program_test_with_subject(String natsSubject) throws Exception {
//         LOGGER.info("Expected response");
//         response = app.getADerProgram(natsSubject);
//     }

    
//     @Then("^the test should complete successfully with a der program response containing:$")
//     public  void the_test_should_complete_successfully_with_a_der_program_response_containing(String expectedJson) throws Exception {
//         Map<String, String > expectedNoFsaMap = new HashMap<>();
//         expectedNoFsaMap.put("message","No DER Programs Found."); 
//         Map<String, String> defaultFsaMap = new HashMap<>();
//         defaultFsaMap.put("activeDERControlListLink", "http://localhost/derp/1/actderc");
//         defaultFsaMap.put("mRID", "A1000000");
//         defaultFsaMap.put("description", "der program fsa");
//         defaultFsaMap.put("version", "1");
//         defaultFsaMap.put("id", "1");
//         defaultFsaMap.put("primacy", "89");
//         defaultFsaMap.put("derCurveListLink", "http://localhost/derp/1/dc");
//         defaultFsaMap.put("defaultDERControlLink", "http://localhost/derp/1/dderc"); 
//         defaultFsaMap.put("derControlListLink", "http://localhost/derp/1/derc");
//         ObjectMapper actualObjectMapper = new ObjectMapper();
//         @SuppressWarnings("unchecked")
//         Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
//         LOGGER.info("the actaul get all function set assignment response is ....." + actualMap);
//         for(Map.Entry<Object, Object> entry:actualMap.entrySet())
//         {
//             Object key = entry.getKey();
            
//             if(key.equals("message") &&  expectedNoFsaMap.containsKey(key))
//             {
//                 scenario.log("actual" + ":" +  actualMap);
//                 scenario.log("expected" + ":" +  expectedNoFsaMap);
//             }
//             else if( key.equals("DERProgram"))
//             {  
//                 {
//                     scenario.log("actual" + ":" + entry.getValue());
//                     scenario.log("expected" + ":" + defaultFsaMap);
//                    } 
//                }
//             } 
          
//         }

    
// }


// /*
//  * 
//  * {FunctionSetAssignments={activeDERControlListLink=http://localhost/derp/1/actderc, derCurveListLink=http://localhost/derp/1/dc, 
//  * defaultDERControlLink=http://localhost/derp/1/dderc, mRID=A1000000, derControlListLink=http://localhost/derp/1/derc,
//  *  description=der program fsa, id=1, primacy=89, version=0}}
//  * 
//  */