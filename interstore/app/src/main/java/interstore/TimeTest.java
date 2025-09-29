package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;

import interstore.DeviceCapability.DcapManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class TimeTest {
    private static final Logger LOGGER = Logger.getLogger(TimeTest.class.getName());
    private static String serviceName;
    public static String TimeResponse;
    public static String timeLink;
    @Autowired
    private DcapManager dcapManager;
    public static String getServiceName(){

        return serviceName;
    }
    public static void setserviceName(String serviceName){

        TimeTest.serviceName = serviceName ;

    }
    public String getTimeResponse() {
        return TimeResponse;
    }

    public static void setTimeResponse(String timeResponseMap) {
        TimeResponse = timeResponseMap;
    }

    public String getTimeQuery(String payload){
        return timeQueryService("get-time", payload);

    }

    public String timeQueryService( String action, String payload)
    {
        Map<String, Object> attributes = new HashMap<>();
        // attributes.put("servicename", getServiceName());
        attributes.put("action", action);
        attributes.put("payload", payload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            Object response = dcapManager.chooseMethod_basedOnAction(postPayload);
            String jsonResponse = new ObjectMapper().writeValueAsString(response);
            getTimeResponse(jsonResponse);
            return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String updateTimeQuery(String payload){
        return timeQueryService("update-time", payload);

    }

    public String getTimeResponse(String responsePayLoad) throws Exception
    {
        LOGGER.info("Raw timeResponse: " + responsePayLoad);

        // If it's wrapped with quotes (starts with " and ends with ")
        if (responsePayLoad.startsWith("\"") && responsePayLoad.endsWith("\"")) {
            responsePayLoad = responsePayLoad.substring(1, responsePayLoad.length() - 1);
        }

        // Replacing escaped quotes
        responsePayLoad = responsePayLoad.replace("\\\"", "\"");

        LOGGER.info("Cleaned timeResponse: " + responsePayLoad);
        setTimeResponse(responsePayLoad);
        LOGGER.info("The Time resource response from DeviceCapability: "+responsePayLoad);
        return responsePayLoad;
    }

    public void getUpdatedTimeResponse(String responsePayLoad) throws Exception
    {

        LOGGER.info("The Time resource was successfully updated.");
    }

    public String getTimeLink(String response){
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
            } 
            else if (jsonObject.has("timeLink")){
                timeLink = jsonObject.getString("timeLink");
                return timeLink;
            }
            else {
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

