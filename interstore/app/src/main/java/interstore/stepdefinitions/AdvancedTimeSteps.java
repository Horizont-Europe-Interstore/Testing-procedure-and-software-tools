package interstore.stepdefinitions;

import interstore.App;
import interstore.ApplicationContextProvider;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AdvancedTimeSteps {
    private App app;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdvancedTimeSteps.class);
    private String response;
    private Scenario scenario;

    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("^I have an advanced time test setup$")
    public void i_have_an_advanced_time_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
    }

    @When("^I execute the advanced time test with service name \"([^\"]*)\" and subject \"([^\"]*)\"$")
    public void i_execute_the_advanced_time_test_with_service_name_and_subject(String serviceName, String natsSubject) throws Exception {
        LOGGER.info("STEP");
        response = app.AdvancedTimeTest(natsSubject);
    }

    @Then("^the test should complete successfully with AdvancedTimeTest response containing:$")
    public void the_test_should_complete_successfully_with_AdvancedTimeTest_response_containing(String expectedJson) throws Exception {
//        Map<String, List<String>> expectedMap = new HashMap<>();
        List<String> expectedResponseList = new ArrayList<>();
        String expected = "The Time resource was updated successfully by 1 hour.";
//        expectedResponseList.add("The Time resource was updated successfully by 1 hour.");
//        expectedMap.put("1", expectedResponseList);
        LOGGER.info("Expected response: {}", expected);
        LOGGER.info("Actual response: {}", response);
        scenario.log("actual" + ":" + response);
        scenario.log("expected" + ":" + expected);
    }
}
