package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DerCurveTest {
    private static final Logger LOGGER = Logger.getLogger(DerCurveTest.class.getName());
    private static String serviceName;
    static String createdDerCurve;
    static String derCurve;

    public static String getserviceName(){
        return serviceName;
    }
    public static void setServicename(String serviceName)
    {
        DerCurveTest.serviceName = serviceName;
    }

    public static String createNewDerCurve(JSONObject payload)
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

    public static String  getADerCurveRequest(Long derpId, Long dercId)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
        attributes.put("action", "get");
        attributes.put("derpID", derpId);
        attributes.put("dercID", dercId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("The payload for the get a Der Curve is " + postPayload);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setADerCurve(String responseDerCurve)
    {
        derCurve = responseDerCurve;
    }

    public static String getADerCurve()
    {
        return derCurve;
    }

    public static void  setCreatedDerCurve(String responseCreateDerCurve)
    {
        createdDerCurve = responseCreateDerCurve;

    }

    public static String getCreatedDerCurve()
    {
        return createdDerCurve;
    }
}
