package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TimeTest {
    private static final Logger LOGGER = Logger.getLogger(MessageFactory.class.getName());
    private static String serviceName;
    public static String TimeResponse;
    public static String timeLink;
    public static String getServiceName(){

        return serviceName;
    }
    public static void setserviceName(String serviceName){

        TimeTest.serviceName = serviceName ;

    }
    public static String getTimeResponse() {
        return TimeResponse;
    }

    public static void setTimeResponse(String timeResponseMap) {
        TimeResponse = timeResponseMap;
    }

    public static String getTimeQuery(String payload){
        return timeQueryService("get-time", payload);

    }

    public static String timeQueryService( String action, String payload)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getServiceName());
        attributes.put("action", action);
        attributes.put("payload", payload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String updateTimeQuery(String payload){
        return timeQueryService("update-time", payload);

    }

    public String getTimeResponse(String responsePayLoad) throws Exception
    {

        setTimeResponse(responsePayLoad);
        LOGGER.info("The Time resource response from DeviceCapability: "+responsePayLoad);
        return responsePayLoad;
    }

    public void getUpdatedTimeResponse(String responsePayLoad) throws Exception
    {

        LOGGER.info("The Time resource was successfully updated.");
    }

    public static String getTimeLink(String response){
        try {
            System.out.println(response);
            if (response.startsWith("\"") && response.endsWith("\"")) {
                response = response.substring(1, response.length() - 1);
            }
            response = response.replace("\\\"", "\"");
            System.out.println(response);
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("time_instance") && jsonObject.has("quality")){
                if (timeLink != null){
                    return timeLink;
                }
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("1");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String url = jsonArray.getString(i);
                    if (url.endsWith("/tm")) {
                        timeLink = url;
                        return timeLink;
                    }
                }
            }
        }
       
        catch (JSONException e) {
            LOGGER.warning("Error parsing JSON: " + e.getMessage());
        } return null;
    }
}

