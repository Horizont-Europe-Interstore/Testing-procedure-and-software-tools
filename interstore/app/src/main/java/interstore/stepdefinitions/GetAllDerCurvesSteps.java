// package interstore.stepdefinitions;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import interstore.App;
// import interstore.ApplicationContextProvider;
// import io.cucumber.java.Before;
// import io.cucumber.java.Scenario;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import java.util.HashMap;
// import java.util.Map;

// public class GetAllDerCurvesSteps {
//     private App app;
//     private static final Logger LOGGER = LoggerFactory.getLogger(GetAllDerCurvesSteps.class);
//     private Object response;
//     private Scenario scenario;

//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }

//     @Given("^I have a get all der curves test setup$")
//     public void i_have_a_get_all_der_curves_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");

//     }

//     @When("^I execute the get all der curves test with subject \"([^\"]*)\"$")
//     public void i_execute_the_get_all_der_curves_test_with_subject(String natsSubject) throws Exception {
//         LOGGER.info("Expected response");
//         response = app.getAllDerCurves(natsSubject);
//     }


//     @Then("^the test should complete successfully with all der curves response containing:$")
//     public  void the_test_should_complete_successfully_with_all_der_curves_response_containing(String expectedJson) throws Exception {
//         Map<String, String > expectedNoderCurveMap = new HashMap<>();
//         expectedNoderCurveMap.put("message","No DER Curves Found.");
//         Map<String, String> defaultderCurveMap = new HashMap<>();

//         defaultderCurveMap.put("mRID", "C0000001");
//         defaultderCurveMap.put("description", "der curve test");
//         defaultderCurveMap.put("version", "1");
//         defaultderCurveMap.put("id", "1");
//         defaultderCurveMap.put("xMultiplier", "9");
//         defaultderCurveMap.put("yMultiplier", "-9");
//         defaultderCurveMap.put("yRefType", "7");
//         ObjectMapper actualObjectMapper = new ObjectMapper();

//         @SuppressWarnings("unchecked")
//         Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
//         LOGGER.info("the actaul get all der curves response is ....." + actualMap);
//         for(Map.Entry<Object, Object> entry:actualMap.entrySet())
//         {
//             Object key = entry.getKey();

//             if(key.equals("message") &&  expectedNoderCurveMap.containsKey(key))
//             {
//                 scenario.log("actual" + ":" +  actualMap);
//                 scenario.log("expected" + ":" +  expectedNoderCurveMap);
//             }
//             else if( key.equals("DERCurves"))
//             {
//                 {
//                     scenario.log("actual" + ":" + entry.getValue());
//                     scenario.log("expected" + ":" + defaultderCurveMap);
//                 }
//             }
//         }

//     }
// }
