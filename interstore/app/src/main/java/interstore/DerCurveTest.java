package interstore;

import org.json.JSONObject;

import java.util.logging.Logger;

public class DerCurveTest {
    private static final Logger LOGGER = Logger.getLogger(DerCurveTest.class.getName());
    private static String serviceName;
    static String createdDerCurve;

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

    public static void  setCreatedDerCurve(String responseCreateDerCurve)
    {
        createdDerCurve = responseCreateDerCurve;

    }

    public static String getCreatedDerCurve()
    {
        return createdDerCurve;
    }
}
