package interstore;

import org.json.JSONObject;

import java.util.logging.Logger;

public class DerControlTest {
    private static final Logger LOGGER = Logger.getLogger(DerControlTest.class.getName());
    private static String serviceName;
    static String createdDerControl;

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
}
