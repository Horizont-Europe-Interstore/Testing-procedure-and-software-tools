package interstore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
public class DeviceCapabilityTest {
    private static final Logger LOGGER = Logger.getLogger(DeviceCapabilityTest.class.getName()); 
    private static String endDeviceListLink;
    // private static String serviceName;
    public static String DeviceCapabilityResponse;
    public static String DeviceCapablities ;   // this may be change into map  
    private static String endDeviceListLinkEndPoint; 
    @Autowired
    private DcapManager dcapManager;

    public DeviceCapabilityTest() {
    
        
    }

    

    // public String natsSubject(){
       
    //     String natsSubject = "dcapmanager" ;
    //     return natsSubject; 
    // }

    // public void setserviceName(String serviceName){    
       
    //      this.serviceName = serviceName ;
    
    // } 
    
    public static void setDeviceCapabilityResponse(String deviceCapabilityResponse) {
        DeviceCapabilityResponse = deviceCapabilityResponse;
    }

    public String getDeviceCapabilityresponse() {
        return DeviceCapabilityResponse;
    } 
 
    
    // public static String getServiceName(){

    //     return serviceName;
    // }
     
   public  static void setDeviceCapablities(String deviceCapablities) {
        DeviceCapablities = deviceCapablities;
    } 
   
    public static String  getDeviceCapablities() {
        return DeviceCapablities;
    } 
    
    public static void setEndDeviceEndPoint(String endDeviceEndpoint) {
        endDeviceListLinkEndPoint = endDeviceEndpoint;
    }
    public static String getEndDeviceEndPoint() {
        return endDeviceListLinkEndPoint;
    }
    public String setPostQuery(Map<String, String> attributes)
    {  
        for(Map.Entry<String, String> entry: attributes.entrySet())
        {
            if(entry.getKey().equals("endDeviceListLink"))
            {
                setEndDeviceEndPoint(entry.getValue());
            }
        }

        
        // attributes.put("servicename", getServiceName());
        attributes.put("action", "post");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createNewDeviceCapability(JSONObject PayLoad) throws JSONException
    {

        String endDeviceendPoint = (String)PayLoad.get("endDeviceListLink");
        setEndDeviceEndPoint(endDeviceendPoint);
        String attributes = new JSONObject()
                // .put("servicename", getServiceName())
                .put("action", "post")
                .put("payload", (Object)PayLoad)
                .toString();
        try {
            LOGGER.info(attributes);
            Object response = dcapManager.chooseMethod_basedOnAction(attributes);
            String jsonResponse = new ObjectMapper().writeValueAsString(response);
            DeviceCapability(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // public String getQuery()
    //  { 
      
    //     Map<String, Object> attributes = new HashMap<>(); 
    // //    attributes.put("servicename", getServiceName());
    //    attributes.put("action", "get");
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     try {
    //         String postPayload = objectMapper.writeValueAsString(attributes);
    //         return postPayload; 
            
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return null; 
    // }

    
   
     public static void setEndDeviceListLink(String EndDeviceListLink) {
        endDeviceListLink = EndDeviceListLink;   
    } 
    
    public static String getEndDeviceListLink() {
        return endDeviceListLink;
    } 
     

    public void AllDeviceCapablity(String payload)
    {
        //LOGGER.info("the device capability response is  " + payload); 
        setDeviceCapablities(payload);  
    }



     // this response should be a map not string and this map 
    public static String DeviceCapability(String responsePayLoad) throws Exception
    {
        LOGGER.info("Response received: "+responsePayLoad);

        setDeviceCapabilityResponse(responsePayLoad); 
        findEndDeviceListLink(responsePayLoad); 
        return responsePayLoad; 
    }


    
    // what to compare here is that while settign up the device capability the user give input /enddevcie 
    // the message received from the nats in the payload . use try , except there 
    
    public static void findEndDeviceListLink(String resposnePayLoad) throws JsonMappingException, JsonProcessingException, JSONException

    {
        try{
            String endDeviceListLinkEndPoint = getEndDeviceEndPoint();
            JSONObject jsonObject = new JSONObject(resposnePayLoad);
            for(String key: ((Map<String, String>) jsonObject).keySet()) // the json object with many keys and keys are int  long corresponding value is list
            {
                JSONArray jsonArray = jsonObject.getJSONArray(key);  // value is list obtaining the vlaue here
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    String link = jsonArray.getString(i);
                    if(link.endsWith(endDeviceListLinkEndPoint))
                    {
                        LOGGER.info("the end device list link is " + link);
                        setEndDeviceListLink(link);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.info("Error: "+e);
        }

    }

} 
  


