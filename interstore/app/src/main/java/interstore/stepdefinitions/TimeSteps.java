// package interstore.stepdefinitions;

// import interstore.App;
// import interstore.ApplicationContextProvider;
// import io.cucumber.java.Before;
// import io.cucumber.java.Scenario;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// public class TimeSteps {
//     private App app;
//     private static final Logger LOGGER = LoggerFactory.getLogger(TimeSteps.class);
//     private String response;
//     private Scenario scenario;

//     @Before
//     public void before(Scenario scenario) {
//         this.scenario = scenario;
//     }

//     @Given("^I have a time test setup$")
//     public void i_have_a_time_test_setup() throws Exception {
//         app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
//     }

//     @When("^I execute the time test with service name \"([^\"]*)\" and subject \"([^\"]*)\"$")
//     public void i_execute_the_time_test_with_service_name_and_subject(String serviceName, String natsSubject) throws Exception {
//         LOGGER.info("STEP");
//         response = app.TimeTest(natsSubject);
//     }

//     @Then("^the test should complete successfully with TimeTest response containing:$")
//     public void the_test_should_complete_successfully_with_TimeTest_response_containing(String expectedJson) throws Exception {
//         Map<String, List<String>> expectedMap = new HashMap<>();
//         List<String> expectedResponseList = new ArrayList<>();
//         expectedResponseList.add("Synchronized the REF-Client Time with the Device Capability");
//         expectedMap.put("1", expectedResponseList);
//         LOGGER.info("Expected response: {}", expectedMap );
//         LOGGER.info("Actual response: {}", response);
//         scenario.log("actual" + ":" + response);
//         scenario.log("expected" + ":" + expectedMap);
//     }

// }
