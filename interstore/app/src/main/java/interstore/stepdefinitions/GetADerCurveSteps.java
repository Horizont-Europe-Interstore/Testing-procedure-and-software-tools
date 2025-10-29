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

// public class GetADerCurveSteps {
//     private App app;
//     private static final Logger LOGGER = LoggerFactory.getLogger(GetADerCurveSteps.class);
//     private Object response;
//     private Scenario scenario;

//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }

//     @Given("^I have a get a der curve test setup$")
//     public void i_have_a_get_a_der_curve_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");

//     }

//     @When("^I execute the get a der curve test with subject \"([^\"]*)\"$")
//     public void i_execute_the_get_a_der_curve_test_with_subject(String natsSubject) throws Exception {
//         LOGGER.info("Expected response");
//         response = app.getADerCurve(natsSubject);
//     }


//     @Then("^the test should complete successfully with a der curve response containing:$")
//     public void the_test_should_complete_successfully_with_a_der_curve_response_containing(String expectedJson) throws Exception {
//         Map<String, String> expectedNoDercMap = new HashMap<>();
//         expectedNoDercMap.put("message", "No DER Curve Found with the provided derpID and dercID.");
//         Map<String, String> defaultDercMap = new HashMap<>();
//         /*
//         * {curveType=2, mRID=B00000000, derCurveLink=http://localhost/derp/1/dc/1,
//         * xMultiplier=0, yRefType=7, description=der curve test, id=1, yMultiplier=-9,
//         * curveData=[{id=1, x_value=10, y_value=25, excitation=false}], version=1}
//         * */
//         defaultDercMap.put("id", "1");
//         defaultDercMap.put("curveType", "2");
//         defaultDercMap.put("mRID", "B0000000");
//         defaultDercMap.put("version", "1");
//         defaultDercMap.put("description", "der curve test");
//         defaultDercMap.put("derCurveLink", "http://localhost/derp/1/dc/1");
//         defaultDercMap.put("xMultiplier", "-9");
//         defaultDercMap.put("yMultiplier", "0");
//         defaultDercMap.put("yRefType", "7");
//         ObjectMapper actualObjectMapper = new ObjectMapper();
//         @SuppressWarnings("unchecked")
//         Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
//         LOGGER.info("the actual get a der curve response is ....." + actualMap);
//         for (Map.Entry<Object, Object> entry : actualMap.entrySet()) {
//             Object key = entry.getKey();

//             if (key.equals("message") && expectedNoDercMap.containsKey(key)) {
//                 scenario.log("actual" + ":" + actualMap);
//                 scenario.log("expected" + ":" + expectedNoDercMap);
//             } else if (key.equals("DERCurve")) {
//                 {
//                     scenario.log("actual" + ":" + entry.getValue());
//                     scenario.log("expected" + ":" + defaultDercMap);
//                 }
//             }
//         }
//     }
// }