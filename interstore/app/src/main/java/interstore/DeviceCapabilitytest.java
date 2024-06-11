package interstore;
import java.util.logging.Logger;
import org.checkerframework.checker.units.qual.s;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException; 
import com.fasterxml.jackson.core.type.TypeReference; 
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.json.JSONArray;
import org.json.JSONException;
public class DeviceCapabilitytest {
    private static final Logger LOGGER = Logger.getLogger(MessageFactory.class.getName()); 
    private static String endDeviceListLink;
    private String serviceName;
    public static String DeviceCapabilityResponse;
    public static String DeviceCapablities ;   // this may be change into map  
    private static String endDeviceListLinkEndPoint; 
    public DeviceCapabilitytest() {
    
        
    }

    

    public String natsSubject(){
       
        String natsSubject = "dcapmanager" ;
        return natsSubject; 
    }

    public void setserviceName(String serviceName){    
       
         this.serviceName = serviceName ;
    
    } 
    
    public static void setDeviceCapabilityResponse(String deviceCapabilityResponse) {
        DeviceCapabilityResponse = deviceCapabilityResponse;
    }

    public static String getDeviceCapabilityresponse() {
        return DeviceCapabilityResponse;
    } 
 
    
    public String getServiceName(){

        return this.serviceName;
    }
     
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

        
        attributes.put("servicename", getServiceName());
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



    public String getQuery()
     { 
      
        Map<String, Object> attributes = new HashMap<>(); 
       attributes.put("servicename", getServiceName());
       attributes.put("action", "get");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            return postPayload; 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }

    
   
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
    public String DeviceCapability(String responsePayLoad) throws Exception
    { 
        //LOGGER.info("Response sent back to NATS for subject: " + responsePayLoad);
        setDeviceCapabilityResponse(responsePayLoad); 
        findEndDeviceListLink(responsePayLoad); 
        return responsePayLoad; 
    }


    
    // what to compare here is that while settign up the device capability the user give input /enddevcie 
    // the message received from the nats in the payload . use try , except there 
    @SuppressWarnings("unchecked")
    public void findEndDeviceListLink(String resposnePayLoad) throws JsonMappingException, JsonProcessingException, JSONException

    { 
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
    }

} 
  



/*
 * // String searchKey =  jsonObject.getString("EndDeviceEndPoint");
 * 
 * 
 */

