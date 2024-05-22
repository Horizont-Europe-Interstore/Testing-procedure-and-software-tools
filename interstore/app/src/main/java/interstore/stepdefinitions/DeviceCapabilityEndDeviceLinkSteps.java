package interstore.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import interstore.App;
import interstore.ApplicationContextProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class DeviceCapabilityEndDeviceLinkSteps {
    

    @Autowired
    private ApplicationContext context;
    private App app;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCapabilityEndDeviceLinkSteps.class);
    private String response;

    @Given("I have a device capability and end device with attributes as response test setup")
    public void i_have_a_device_capability_and_end_device_with_attributes_as_response_test_setup() throws Exception {
           
    }

    @When("I execute the end device link test with subject {string}")
    public void i_execute_the_end_device_link_test_with_subject(String natsSubject) throws Exception {
       //response = app.DeviceCapabilityEndDeviceLinkTest(natsSubject);
    }

    @Then("the test should complete successfully with EndDevice response with attributes containing:")
    public void then_the_test_should_complete_successfully_with_EndDevice_response_with_attributes_containing(String expectedJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expected = objectMapper.readTree(expectedJson);
        JsonNode actual = objectMapper.readTree(response);
        LOGGER.info("Expected response: {} and actual response is {}", expected, actual);
        assertEquals(expected, actual, "The actual response does not match the expected response.");
    }
}
