package interstore;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import interstore.DER.DerManager;

@Component
public class DerTest {
    private static final Logger LOGGER = Logger.getLogger(DerTest.class.getName());
    static String createdDerCapability;
    static String derCapability;
    static String derEntity;
    static String createdDerSettings;
    static String derSettings; 
    static String editedpowerGeneration;
    static String powerGeneration;
    @Autowired
    private DerManager derManager;

    
   public String createNewDerCapability(JSONObject payload) 
   {
       try {
           String attributes = new JSONObject()
                   .put("action", "post")
                   .put("payload", (Object)payload)
                   .put("derCapabilities", "derCapabilities")
                   .toString();
           LOGGER.info(attributes);
           Object response = derManager.chooseMethod_basedOnAction(attributes);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setcreatedDerCapability(jsonResponse);
           return jsonResponse;
           
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null ;
   }
   
   public static void setcreatedDerCapability(String responseCreateDerCapability)
   { 
    createdDerCapability = responseCreateDerCapability;
       
   }
   
   public String getCreatedDerCapability()
   {
       return createdDerCapability;
   } 
    
   public String getADerCapabilityRequest(Long derId , Long endDeviceId )
   {
       Map<String, Object> attributes = new HashMap<>();
       attributes.put("action", "get");
       attributes.put("endDeviceID", endDeviceId);
       attributes.put("derCapabilities", "derCapabilities"); 
       attributes.put("derID", derId);
       ObjectMapper objectMapper = new ObjectMapper();
       try {
           String postPayload = objectMapper.writeValueAsString(attributes);
           Object response = derManager.chooseMethod_basedOnAction(postPayload);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setADerCapability(jsonResponse);
           return jsonResponse;
           

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }

   public static void setADerCapability(String responsederCapability)
   {
    derCapability = responsederCapability;
   } 

   public String getADerCapability()
   {
    return derCapability; 
   }

   public static void setADer(String responsederEntity)
   {
    derEntity = responsederEntity;
   } 

   public String getADer()
   {
    return derEntity; 
   }

   public String createNewDer(JSONObject payload) 
   {
       try {
           String attributes = new JSONObject()
                   .put("action", "post")
                   .put("payload", (Object)payload)
                   .toString();
           LOGGER.info(attributes);
           Object response = derManager.chooseMethod_basedOnAction(attributes);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setADer(jsonResponse);
           return jsonResponse;

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null ;
   }
  
   public String createNewDerSettings(JSONObject payload) 
   {
       try {
           String attributes = new JSONObject()
                   .put("action", "post")
                   .put("derSettings", "derSettings")
                   .put("payload", (Object)payload)
                   .toString();
           LOGGER.info(attributes);
           Object response = derManager.chooseMethod_basedOnAction(attributes);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setcreatedDerSettings(jsonResponse);
           return jsonResponse;

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null ;
   }

   public static void setcreatedDerSettings(String responseCreateDerSettings)
   {
    createdDerSettings = responseCreateDerSettings;

   }
   
   public String getCreatedDerSettings()
   {
       return createdDerSettings;
   }

   public String getADerSettingsRequest(Long derId , Long endDeviceId )
   {
       Map<String, Object> attributes = new HashMap<>();
       attributes.put("action", "get");
       attributes.put("derSettings", "derSettings"); 
       attributes.put("endDeviceID", endDeviceId);
       attributes.put("derID", derId);
       ObjectMapper objectMapper = new ObjectMapper();
       try {
           String postPayload = objectMapper.writeValueAsString(attributes);
           Object response = derManager.chooseMethod_basedOnAction(postPayload);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setADerSettings(jsonResponse);
           return jsonResponse;

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
   
   public String getADerRequest(Long derId , Long endDeviceId )
   {
       Map<String, Object> attributes = new HashMap<>();
       attributes.put("action", "get");
       attributes.put("endDeviceId", endDeviceId);
       attributes.put("derID", derId);
       ObjectMapper objectMapper = new ObjectMapper();
       try {
           String postPayload = objectMapper.writeValueAsString(attributes);
           Object response = derManager.chooseMethod_basedOnAction(postPayload);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setADerSettings(jsonResponse);
           return jsonResponse;

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
   
   public static void setADerSettings(String responsederSettings)
   {
    derSettings = responsederSettings;
   } 

   public String getADerSettings()
   {
    return derSettings; 
   }
  
   public String powerGenerationDeviceTest(JSONObject payload) 
   {
       try {
           String attributes = new JSONObject()
                   .put("action", "put")
                   .put("powergeneration", "powergeneration")
                   .put("payload", (Object)payload)
                   .toString();
           LOGGER.info(attributes);
           Object response = derManager.chooseMethod_basedOnAction(attributes);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setEditedpowerGeneration(jsonResponse);
           return jsonResponse;

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null ;
   }
   public void setEditedpowerGeneration(String responsepowerGeneration)
   {
    editedpowerGeneration = responsepowerGeneration;

   }
   public String getEditedpowerGeneration()
   {
       return editedpowerGeneration;
   }

}
