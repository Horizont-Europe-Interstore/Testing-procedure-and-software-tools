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

// public class GetAllDerControlsSteps {
//     private App app;
//     private static final Logger LOGGER = LoggerFactory.getLogger(GetAllDerControlsSteps.class);
//     private Object response;
//     private Scenario scenario;

//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }

//     @Given("^I have a get all der controls test setup$")
//     public void i_have_a_get_all_der_controls_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");

//     }

//     @When("^I execute the get all der controls test with subject \"([^\"]*)\"$")
//     public void i_execute_the_get_all_der_controls_test_with_subject(String natsSubject) throws Exception {
//         LOGGER.info("Expected response");
//         response = app.getAllDerControls(natsSubject);
//     }


//     @Then("^the test should complete successfully with all der controls response containing:$")
//     public  void the_test_should_complete_successfully_with_all_der_controls_response_containing(String expectedJson) throws Exception {
//         Map<String, String > expectedNoDerControlMap = new HashMap<>();
//         expectedNoDerControlMap.put("message","No DER Controls Found.");
//         Map<String, String> defaultDerControlMap = new HashMap<>();

//         defaultDerControlMap.put("deviceCategory", "9");
//         defaultDerControlMap.put("derControlLink", "http://localhost/derp/1/derc/1");
//         defaultDerControlMap.put("id", "1");
//         defaultDerControlMap.put("derControlBase", "opModFixedW=1, opModFixedPFAbsorbW=null, " +
//                 "opModLFRTMustTrip=null, opModHFRTMustTrip=null, opModLFRTMayTrip=null, opModLVRTMustTrip=null, " +
//                 "opModHVRTMustTrip=1, opModTargetW=null, opModFreqDroop=null, opModVoltWatt=null, opModFreqWatt=null, " +
//                 "opModLVRTMomentaryCessation=null, opModLVRTMayTrip=null, rampTms=null, opModTargetVar=null, " +
//                 "opModEnergize=null, opModHVRTMayTrip=null, opModVoltVar=null, opModConnect=null, opModFixedPFInjectW=null, " +
//                 "opModWattPF=null, opModWattVar=null, opModMaxLimW=null, opModFixedVar=null, " +
//                 "opModHFRTMayTrip=null, opModHVRTMomentaryCessation=null");
//         ObjectMapper actualObjectMapper = new ObjectMapper();

//         @SuppressWarnings("unchecked")
//         Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
//         LOGGER.info("the actaul get all der controls response is ....." + actualMap);
//         for(Map.Entry<Object, Object> entry:actualMap.entrySet())
//         {
//             Object key = entry.getKey();

//             if(key.equals("message") &&  expectedNoDerControlMap.containsKey(key))
//             {
//                 scenario.log("actual" + ":" +  actualMap);
//                 scenario.log("expected" + ":" +  expectedNoDerControlMap);
//             }
//             else if( key.equals("DERControls"))
//             {
//                 {
//                     scenario.log("actual" + ":" + entry.getValue());
//                     scenario.log("expected" + ":" + defaultDerControlMap);
//                 }
//             }
//         }

//     }
// }
