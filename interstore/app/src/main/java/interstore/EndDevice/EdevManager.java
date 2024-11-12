package interstore.EndDevice;

import interstore.EndDeviceTest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
@RestController
public class EdevManager {
    private EndDeviceImpl endDeviceImpl;
    private EndDeviceTest endDeviceTest;
    private static final Logger LOGGER = Logger.getLogger(EdevManager.class.getName());
    private static String endDeviceEndPoint; 
    private static String registerationendpoint;
    private static String functionsetassignmentEndPoint;
    private static String deviceStatusLinkendPoint;

    public EdevManager(EndDeviceImpl endDeviceImpl) {
        this.endDeviceImpl = endDeviceImpl;
    }

    public Map<String, Object> chooseMethod_basedOnAction(String payload) throws InterruptedException, JSONException{
        if (payload == null || payload.isEmpty()) {
            throw new IllegalArgumentException("payload cannot be null or empty");
        }
        JSONObject jsonObject = new JSONObject(payload);
        if(!jsonObject.has("action"))
        {
            throw new IllegalArgumentException("action cannot be null or empty");
        }
        String action = jsonObject.getString("action");
        switch(action){
            case"post":
            return identify_method(jsonObject);
            case "get":
                   return payLoadParser(jsonObject); 
            case "put":
                updateEndDevice(jsonObject);
                return null;  // change it to map later 
            case "delete":
                deleteEndDevice(jsonObject.getString("id"));
                return null;  // change it to map later 

        }
        return null;  // change it to map later with a default value with invalid operation 
    }
    
   /* the best solution is that the regex patterns and try to match those regex patterns 
    * the basic regex matches with all other paterns 
    eceived message from NATS{"payload":"http://localhost/edev","action":"get","servicename":"enddevicemanager"}
    */


    public  Map<String, Object> payLoadParser(JSONObject jsonObject) throws JSONException 
    {  
    if(jsonObject.has("endDeviceID"))  
    {
        Long endDeviceID = jsonObject.getLong("endDeviceID");
        if (jsonObject.has("registrationID")) {
            Long registrationID = jsonObject.getLong("registrationID");
            return this.getRegisteredEndDeviceDetails(endDeviceID, registrationID);
        } else 
        {
            return this.getEndDeviceById(endDeviceID);
        }    
    }
     else {
            if(jsonObject.has("endDeviceLink"))
              {
                try {
                    return this.getEndevices();
                } catch (JsonProcessingException e) {
                    LOGGER.info("error occured while getting all end devices" +  e);
                }
              }
            } 
            return null; 
        } 
       
         
    
   
    public Map<String, Object> addEndDevice( JSONObject jsonPayLoad) throws InterruptedException {
        EndDeviceDto endDeviceDto = this.endDeviceImpl.createEndDevice(jsonPayLoad);
        return this.endDeviceImpl.getEndDeviceID(endDeviceDto);  
       
    } 
    

    public Map<String, Object> registerEndDevice( JSONObject jsonPayLoad) throws InterruptedException, JSONException {
        Long endDeviceID = jsonPayLoad.getLong("endDeviceID");
        Long registrationPin = jsonPayLoad.getLong("pin");
        return  this.endDeviceImpl.registerEndDevice( registrationPin, endDeviceID);   
    }
     
    
    
    public  Map<String, Object> identify_method(JSONObject jsonObject) throws InterruptedException, JSONException
    {  
        if(jsonObject.has("endDeviceID"))
        {
          return registerEndDevice(jsonObject);
            
        }
        else {
            return addEndDevice(jsonObject);
        }
    }


   public static void SetEndDeviceEndpoint(String endpoint)
    {
      endDeviceEndPoint = endpoint; 
   }

   public String getEndDeviceEndpoint()
     {
      return endDeviceEndPoint;
     } 

     public static void setregistrationEndpoint(String endpoint)
     {
        registerationendpoint = endpoint;
     } 

    public static String getregistrationEndpoint()
    {
        return registerationendpoint;
    }
    public static void setfunctionsetAssignmentEndpoint(String endpoint)
     {
        functionsetassignmentEndPoint = endpoint;
     }
     public static String getfunctionsetAssignmentEndpoint()
     {
        return functionsetassignmentEndPoint;
     }

     public static void setDeviceStatusLinkendPoint(String endpoint)
     {
        deviceStatusLinkendPoint = endpoint;
     }
     public static String getDeviceStatusLinkendPoint()
     {
        return deviceStatusLinkendPoint;
     }


     
    @GetMapping("/edev")
    public Map<String, Object> getEndevices() throws JsonProcessingException {
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getAllEndDevices();
        return utiltyGetallEndDeviceLinks(responseEntity);
    }

     @GetMapping("/edev/{id}")
    public Map<String, Object>getEndDeviceById(@PathVariable Long id) 
    {   
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getEndDevice(id);
        Map<String, Object> responseMap = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        
        return responseEntity.getBody(); 
    }

    @GetMapping("/edev/{id}/rg")
    public Map<String, Object> getEndDeviceRegistrationLink(@PathVariable Long endDeviceID) {
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getAllRegisteredEndDevice( endDeviceID);
        return responseEntity.getBody(); 
    }
   
     @GetMapping("/edev/{endDeviceID}/rg/{registrationID}")
     public Map<String, Object> getRegisteredEndDeviceDetails(@PathVariable Long endDeviceID, @PathVariable Long registrationID) {
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getRegisterdEndDeviceDetails( endDeviceID, registrationID);
        return responseEntity.getBody();
     }
    
    public Map<String, Object> utiltyGetallEndDeviceLinks(ResponseEntity<Map<String, Object>> response) throws JsonProcessingException
      { 
        ObjectMapper objectMapper = new ObjectMapper(); 
        List<Object> endDeviceLinks = new ArrayList(); 
         Map<String, Object> responseMap = new HashMap(); 
         List<Object> endDeviceList = new ArrayList<>();
         Map<String, Object> listofEndDevicesLink = response.getBody();
         String endDeviceLink = "endDeviceLink";
         LOGGER.info("the list of end device links is " + listofEndDevicesLink);
         for(Map.Entry<String, Object>entry : listofEndDevicesLink.entrySet())
         {
            String endDeviceLinkkey = entry.getKey();
            LOGGER.info("the key is " + endDeviceLinkkey); 
             if (endDeviceLinkkey.equals("message"))
             {
                LOGGER.info("the message is " + entry.getValue());
                return listofEndDevicesLink;
             }
             else
             {
                LOGGER.info("the message is " + entry.getValue());
                endDeviceList.add(entry.getValue());
             }
            
         } 
        for(Object item : endDeviceList)
               {
                try {
                    
                    String jsonString = objectMapper.writeValueAsString(item); 
                    LOGGER.info("the new string is " + jsonString); 
                    JsonNode jsonArray = objectMapper.readTree(jsonString);
                    for(JsonNode node : jsonArray)
                    {
                        if(node.has(endDeviceLink))
                        {  
                            String jsonEndDeviceLink = node.get("endDeviceLink").asText();
                            LOGGER.info("endDeviceLink in JSON: " + jsonEndDeviceLink);
                            endDeviceLinks.add(jsonEndDeviceLink);
                        }
                    }
                } 
               catch (JsonProcessingException e) {
                LOGGER.info("the exception is " + e.getMessage());
            } 
            responseMap.put("endDevices", endDeviceLinks); 
            return responseMap;   
        
        } 
         return null;
      }
 

     public EndDeviceDto updateEndDevice( JSONObject jsonObject) {
        return null;
    }
    public void deleteEndDevice( String id) {

    }
    
}













/*

 


     public Object splitPayload(String payload) {
        String normalizedPayload = payload.replaceAll("^https?://", "");
       
        int startIndex = normalizedPayload.indexOf("/");  
        if (startIndex != -1) {                            
            String path = normalizedPayload.substring(startIndex + 1);   
            String[] parts = path.split("/");                                                                                    
            for (String part : parts) {
                System.out.println(part); 
            }
            switch (parts.length) {
                case 1:
                    return parts[0];
                case 2:
                    try {
                        return Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        return parts[1]; 
                    }
                case 4: 
                    try {
                        long id1 = Long.parseLong(parts[1]);
                        long id2 = Long.parseLong(parts[3]);
                        return new String[] {String.valueOf(id1), parts[2], String.valueOf(id2)};
                    } catch (NumberFormatException e) {
                        return "Invalid Numeric values in URL path";
                    }
                default:
                    return "Unsupported URL format"; 
            }
        }
        return "No path found";
    }
    

   


 public String getEndDeviceInstance( JSONObject jsonPayLoad) {
       try{
        String payload = jsonPayLoad.optString("payload");
        Pattern pattern = Pattern.compile("(?i).*\\/[a-z]+\\/(\\d+)\\/[a-z]+");
        Matcher matcher = pattern.matcher(payload);
     if (matcher.find()) {
         String id = matcher.group(1); // This will give you the ID '1234'
         System.out.println("ID: " + id);
         return id;
     } 
       } catch (PatternSyntaxException e) {
        System.err.println("Regex pattern syntax error: " + e.getDescription());
       
    } catch (Exception e) {
        System.err.println("An error occurred while processing the payload: " + e.getMessage());
        
    }
    return null;
}

 try
        {   
            //Object response = splitPayload(payload);
            if(response instanceof String)
        {
            
        } 
        else if(response instanceof Long)
        {
            Long endDeviceId = (Long) response;
            return this.getEndDeviceById(endDeviceId);
        }
        else if (response instanceof String[])
        {   
            String[] parts = (String[]) response;
            if(parts[1].equals(getregistrationEndpoint()))  
            {   
                Long endDeviceId = Long.parseLong(parts[0]);
                Long registeredEndDeviceId = Long.parseLong(parts[2]);
                return this.getRegisteredEndDeviceDetails(endDeviceId, registeredEndDeviceId);
            }
        }
        } catch (PatternSyntaxException e) {
         
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    return null;
      









 */