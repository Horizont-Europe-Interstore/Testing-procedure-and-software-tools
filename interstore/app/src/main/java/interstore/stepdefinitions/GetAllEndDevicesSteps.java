package interstore.stepdefinitions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import interstore.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import interstore.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class GetAllEndDevicesSteps {
    private App app; 
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAllEndDevicesSteps.class);
    private Object response;

    @Given("^I have a device capability and end device test setup$")
    public void i_have_a_device_capability_and_end_device_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
    }

    @When("^I execute the end device test with subject \"([^\"]*)\"$")
    public void i_execute_the_end_device_test_with_subject(String natsSubject) throws Exception {
         response = app.getAllEndDevicesTest(natsSubject);
    }



    @Then("^the test should complete successfully with EndDevice response containing:$")
    public void then_the_test_should_complete_successfully_with_EndDevice_response_containing(String expectedJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expected = objectMapper.readTree(expectedJson);
        JsonNode actual = objectMapper.readTree((JsonParser) response);
        LOGGER.info("Expected response: {} and actual response is {}", expected, actual);
        assertEquals(expected, actual, "The actual response does not match the expected response.");
    }
    
}

