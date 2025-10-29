package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;

import interstore.DERControl.DERControlManager;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class DerControlTest {
    private static final Logger LOGGER = Logger.getLogger(DerControlTest.class.getName());
    static String createdDerControl;
    static String derControl;
    static String listOfDerControls;
    @Autowired
    private DERControlManager derControlManager;

    public String createNewDerControl(JSONObject payload)
    {
        try {
            String attributes = new JSONObject()
                    .put("action", "post")
                    .put("payload", (Object)payload)
                    .toString();
            LOGGER.info(attributes);
            Object response = derControlManager.chooseMethod_basedOnAction(attributes);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setCreatedDerControl(jsonResponse);
           return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    public void setCreatedDerControl(String responseCreateDerControl)
    {
        LOGGER.info("Reached in DERControl Test with response: " + responseCreateDerControl);
        createdDerControl = responseCreateDerControl;

    }

    public String getCreatedDerControl()
    {
        return createdDerControl;
    }

    public String getAllDerControlRequest(Long derpID)
    {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("action", "get");
        attributes.put("derpID", derpID);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("The payload for the get all Der Control is " + postPayload);
            Object response = derControlManager.chooseMethod_basedOnAction(postPayload);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setAllDerControls(jsonResponse);
           return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String setAllDerControls(String responseAllDerControls) {
        LOGGER.info("The DerControls for the given derProgram is "+ responseAllDerControls);
        listOfDerControls = responseAllDerControls;
        return responseAllDerControls;
    }

    public String getAllderControls(){
        return listOfDerControls;
    }

    public String getADerControlRequest(Long derpId, Long derControlId)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("action", "get");
        attributes.put("derpID", derpId);
        attributes.put("derControlID", derControlId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("The payload for the get a Der Control is " + postPayload);
            Object response = derControlManager.chooseMethod_basedOnAction(postPayload);
            String jsonResponse = new ObjectMapper().writeValueAsString(response);
            setADerControl(jsonResponse);
            return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setADerControl(String responseDerControl)
    {
        derControl = responseDerControl;
    }

    public String getADerControl()
    {
        return derControl;
    }
}
