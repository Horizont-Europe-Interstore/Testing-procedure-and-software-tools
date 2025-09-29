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

// public class GetADerControlSteps {
//     private App app;
//     private static final Logger LOGGER = LoggerFactory.getLogger(GetADerControlSteps.class);
//     private Object response;
//     private Scenario scenario;

//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }

//     @Given("^I have a get a der control test setup$")
//     public void i_have_a_get_a_der_control_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");

//     }

//     @When("^I execute the get a der control test with subject \"([^\"]*)\"$")
//     public void i_execute_the_get_a_der_control_test_with_subject(String natsSubject) throws Exception {
//         LOGGER.info("Expected response");
//         response = app.getADerControl(natsSubject);
//     }


//     @Then("^the test should complete successfully with a der control response containing:$")
//     public void the_test_should_complete_successfully_with_a_der_control_response_containing(String expectedJson) throws Exception {
//         Map<String, String> expectedNoDercMap = new HashMap<>();
//         expectedNoDercMap.put("message", "No DER Control Found with the provided derpID and derControlID.");
//         Map<String, String> defaultDercMap = new HashMap<>();

//         defaultDercMap.put("id", "1");
//         defaultDercMap.put("deviceCategory", "9");
//         defaultDercMap.put("derControlLink", "http://localhost/derp/1/derc/1");
//         defaultDercMap.put("derControlBase", "opModFixedW=1, opModFixedPFAbsorbW=null, " +
//                 "opModLFRTMustTrip=null, opModHFRTMustTrip=null, opModLFRTMayTrip=null, opModLVRTMustTrip=null, " +
//                 "opModHVRTMustTrip=1, opModTargetW=null, opModFreqDroop=null, opModVoltWatt=null, opModFreqWatt=null, " +
//                 "opModLVRTMomentaryCessation=null, opModLVRTMayTrip=null, rampTms=null, opModTargetVar=null, " +
//                 "opModEnergize=null, opModHVRTMayTrip=null, opModVoltVar=null, opModConnect=null, opModFixedPFInjectW=null, " +
//                 "opModWattPF=null, opModWattVar=null, opModMaxLimW=null, opModFixedVar=null, " +
//                 "opModHFRTMayTrip=null, opModHVRTMomentaryCessation=null");

//         ObjectMapper actualObjectMapper = new ObjectMapper();
//         @SuppressWarnings("unchecked")
//         Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
//         LOGGER.info("the actual get a der control response is ....." + actualMap);
//         for (Map.Entry<Object, Object> entry : actualMap.entrySet()) {
//             Object key = entry.getKey();

//             if (key.equals("message") && expectedNoDercMap.containsKey(key)) {
//                 scenario.log("actual" + ":" + actualMap);
//                 scenario.log("expected" + ":" + expectedNoDercMap);
//             } else if (key.equals("DERControl")) {
//                 {
//                     scenario.log("actual" + ":" + entry.getValue());
//                     scenario.log("expected" + ":" + defaultDercMap);
//                 }
//             }
//         }
//     }
// }
