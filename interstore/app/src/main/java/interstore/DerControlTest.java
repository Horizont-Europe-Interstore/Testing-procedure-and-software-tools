package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DerControlTest {
    private static final Logger LOGGER = Logger.getLogger(DerControlTest.class.getName());
    private static String serviceName;
    static String createdDerControl;
    static String derControl;
    static String listOfDerControls;

    public static String getserviceName(){
        return serviceName;
    }
    public static void setServicename(String serviceName)
    {
        DerControlTest.serviceName = serviceName;
    }

    public static String createNewDerControl(JSONObject payload)
    {
        try {
            String attributes = new JSONObject()
                    .put("servicename", getserviceName())
                    .put("action", "post")
                    .put("payload", (Object)payload)
                    .toString();
            LOGGER.info(attributes);
            return attributes;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static void  setCreatedDerControl(String responseCreateDerControl)
    {
        LOGGER.info("Reached in DERControl Test with response: " + responseCreateDerControl);
        createdDerControl = responseCreateDerControl;

    }

    public static String getCreatedDerControl()
    {
        return createdDerControl;
    }

    public static String getAllDerControlRequest(Long derpID)
    {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
        attributes.put("action", "get");
        attributes.put("derpID", derpID);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("The payload for the get all Der Control is " + postPayload);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String setAllDerControls(String responseAllDerControls) {
        LOGGER.info("The DerControls for the given derProgram is "+ responseAllDerControls);
        listOfDerControls = responseAllDerControls;
        return responseAllDerControls;
    }

    public static String getAllderControls(){
        return listOfDerControls;
    }

    public static String  getADerControlRequest(Long derpId, Long derControlId)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
        attributes.put("action", "get");
        attributes.put("derpID", derpId);
        attributes.put("derControlID", derControlId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("The payload for the get a Der Control is " + postPayload);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setADerControl(String responseDerControl)
    {
        derControl = responseDerControl;
    }

    public static String getADerControl()
    {
        return derControl;
    }
}
