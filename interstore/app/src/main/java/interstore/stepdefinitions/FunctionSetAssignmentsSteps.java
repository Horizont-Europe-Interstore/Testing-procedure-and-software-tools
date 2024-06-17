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

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FunctionSetAssignmentsSteps {
    private App app;
    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionSetAssignmentsSteps.class);
    private String response;
    private Scenario scenario;

    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("^I have a fsa test setup$")
    public void i_have_a_fsa_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
    }

    @When("^I execute the fsa test with service name \"([^\"]*)\" and subject \"([^\"]*)\"$")
    public void i_execute_the_fsa_test_with_service_name_and_subject(String serviceName, String natsSubject) throws Exception {
        LOGGER.info("STEP");
        response = app.FunctionSetAssignmentTest(natsSubject);
    }

    @Then("^the test should complete successfully with FunctionSetAssignmentTest response containing:$")
    public void the_test_should_complete_successfully_with_FunctionSetAssignmentTest_response_containing(String expectedJson) throws Exception {
        String expected = "Found DERPrograms instances";
        LOGGER.info("Expected response: {}", expected);
        LOGGER.info("Actual response: {}", response);
        scenario.log("actual" + ":" + response);
        scenario.log("expected" + ":" + expected);
        assertTrue(expected.equals(response), "The actual response does not match the expected response.");
    }
}
