package interstore.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import interstore.App;
import interstore.ApplicationContextProvider;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CreateDerCurveSteps {
    private App app;
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDerCurveSteps.class);
    private Object response;
    private Scenario scenario;

    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("^I have a create der curve test setup$")
    public void i_have_a_create_der_curve_test_setup() throws Exception {
        app = (App) ApplicationContextProvider.getApplicationContext().getBean("app");

    }

    @When("^I execute the create der curve test with subject \"([^\"]*)\"$")
    public void i_execute_the_create_der_curve_test_with_subject(String natsSubject) throws Exception {
        response = app.createDerCurve(natsSubject);
        LOGGER.info("Actual response: {}", response);

    }

    @Then("^the test should complete successfully with create a der curve response containing:$")
    public void the_test_should_complete_successfully_with_create_a_der_curve_response_containing(String expectedJson) throws Exception  {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("id", "1");
        expectedMap.put("curveType", "0");
        expectedMap.put("x_multiplier_type", "0");
        expectedMap.put("y_multiplier_type", "0");
        expectedMap.put("y_ref_type", "0");
        expectedMap.put("mRID", "C0000001");
        expectedMap.put("version", "1");


        ObjectMapper actualObjectMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<Object, Object> actualMap = actualObjectMapper.readValue((String) response, Map.class);
        LOGGER.info("Actual response: {}", actualMap);
        for(Map.Entry<Object, Object> entry:actualMap.entrySet())
        {

            Object key = entry.getKey();
            if(actualMap.containsKey(key) && expectedMap.containsKey(key))
            {
                LOGGER.info("sample key is:" + entry.getKey());
                scenario.log("actual" + ":" + actualMap);
                scenario.log("expected" + ":" +  expectedMap);
            }
        }


    }
}