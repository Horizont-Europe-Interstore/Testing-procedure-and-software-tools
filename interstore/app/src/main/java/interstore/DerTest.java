package interstore;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DerTest {
    private static final Logger LOGGER = Logger.getLogger(DerTest.class.getName());
    private static String serviceName;
   
    static String createdDerCapability;
    static String derCapability;
    
    public static String getserviceName(){
        return serviceName;
    }

    public static void setServicename(String serviceName)
    {
        DerTest.serviceName = serviceName;
    }

    
   public static String createNewDerCapability(JSONObject payload) 
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
   
   public static void setcreatedDerCapability(String responseCreateDerCapability)
   { 
    createdDerCapability = responseCreateDerCapability;
       
   }
   
   public static String getCreatedDerCapability()
   {
       return createdDerCapability;
   } 
    
   public static String getADerCapabilityRequest(Long derId , Long endDeviceId )
   {
       Map<String, Object> attributes = new HashMap<>();
       attributes.put("servicename", getserviceName());
       attributes.put("action", "get");
       attributes.put("endDeviceId", endDeviceId);
       attributes.put("derID", derId);
       ObjectMapper objectMapper = new ObjectMapper();
       try {
           String postPayload = objectMapper.writeValueAsString(attributes);
           LOGGER.info("The payload for the get a Der Capability is " + postPayload);
           return postPayload;

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }

   public static void setADerCapability(String responsederCapability)
   {
    derCapability = responsederCapability;
   } 

   public static String getADerCapability()
   {
    return derCapability; 
   }

}
