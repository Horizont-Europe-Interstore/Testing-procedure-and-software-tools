package interstore.stepdefinitions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;
import interstore.ApplicationContextProvider;
import interstore.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceCapabilitySteps {

   // @Autowired
    private App app; 
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCapabilitySteps.class);
    private String response;
   

    @Given("^I have a device capability test setup$")
    public void i_have_a_device_capability_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");
    }

    @When("^I execute the device capability test with service name \"([^\"]*)\" and subject \"([^\"]*)\"$")
    public void i_execute_the_device_capability_test_with_service_name_and_subject(String serviceName, String natsSubject) throws Exception {
        response = app.CreateDeviceCapabilityTest(natsSubject); 
    }

    @Then("^the test should complete successfully with DeviceCapability response containing:$")
    public void the_test_should_complete_successfully_with_DeviceCapability_response_containing(String expectedJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
       JsonNode expected = objectMapper.readTree(expectedJson);
        JsonNode actual = objectMapper.readTree(response);
        Set<String> expectedSet = new HashSet<>();
       expected.get("1").forEach(node -> expectedSet.add(node.asText()));

       Set<String> actualSet = new HashSet<>();
       actual.get("1").forEach(node -> actualSet.add(node.asText()));
       LOGGER.info("Expected response: {}", expected + "and " + "actual response is "+ actual);
      assertTrue(expectedSet.equals(actualSet), "The actual response does not match the expected response.");

    }

} 


// ./gradlew cucumber -Dcucumber.feature=src/test/resources/interstore/DeviceCapability.feature

/*
 * "1":["http://localhost/interstore/sdev",
     "http://localhost/interstore/mup",
     "http://localhost/interstore/edev"]
    
 * Process process = Runtime.getRuntime().exec("path/to/hello.sh"); 
 * 
 */

 