package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;

import interstore.DERCurve.DERCurveManager;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class DerCurveTest {
    private static final Logger LOGGER = Logger.getLogger(DerCurveTest.class.getName());
    static String createdDerCurve;
    static String derCurve;
    static String listOfDerCurves;
    @Autowired
    private DERCurveManager derCurveManager;

    public String createNewDerCurve(JSONObject payload)
    {
        try {
            String attributes = new JSONObject()
                    .put("action", "post")
                    .put("payload", (Object)payload)
                    .toString();
            LOGGER.info(attributes);
            Object response = derCurveManager.chooseMethod_basedOnAction(attributes);
            String jsonResponse = new ObjectMapper().writeValueAsString(response);
            setCreatedDerCurve(jsonResponse);
            return jsonResponse;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    public String getADerCurveRequest(Long derpId, Long dercId)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("action", "get");
        attributes.put("derpID", derpId);
        attributes.put("dercID", dercId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("The payload for the get a Der Curve is " + postPayload);
            Object response = derCurveManager.chooseMethod_basedOnAction(postPayload);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setADerCurve(jsonResponse);
           return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setADerCurve(String responseDerCurve)
    {
        derCurve = responseDerCurve;
    }

    public String getADerCurve()
    {
        return derCurve;
    }

    public void setCreatedDerCurve(String responseCreateDerCurve)
    {
        createdDerCurve = responseCreateDerCurve;

    }

    public String getCreatedDerCurve()
    {
        return createdDerCurve;
    }

    public String getAllDerCurveRequest(Long derpID)
    {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("action", "get");
        attributes.put("derpID", derpID);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("The payload for the get all Der Curve is " + postPayload);
            Object response = derCurveManager.chooseMethod_basedOnAction(postPayload);
            String jsonResponse = new ObjectMapper().writeValueAsString(response);
            setAllderCurves(jsonResponse);
            return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String setAllderCurves(String responseAllDerCurves) {
        LOGGER.info("The DerCurves for the given derProgram is "+ responseAllDerCurves);
        listOfDerCurves = responseAllDerCurves;
        return responseAllDerCurves;
    }

    public String getAllderCurves(){
        return listOfDerCurves;
    }


}
