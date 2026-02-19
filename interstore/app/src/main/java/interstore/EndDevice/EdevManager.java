package interstore.EndDevice;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class EdevManager {
    private EndDeviceService endDeviceImpl;
    private static final Logger LOGGER = Logger.getLogger(EdevManager.class.getName());
    private static String endDeviceEndPoint; 
    private static String registerationendpoint;
    private static String functionsetassignmentEndPoint;
    private static String deviceStatusLinkendPoint;

    public EdevManager(EndDeviceService endDeviceImpl) {
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
                return null;  
            case "delete":
                deleteEndDevice(jsonObject.getString("id"));
                return null;  
        }
        return null;  
    }
    
   

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
        EndDeviceEntity endDeviceDto = this.endDeviceImpl.createEndDevice(jsonPayLoad);
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


     
    
    public Map<String, Object> getEndevices() throws JsonProcessingException {
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getAllEndDevices();
        return utiltyGetallEndDeviceLinks(responseEntity);
    }

    @GetMapping(value = "/edev", produces = "application/sep+xml")
    public ResponseEntity<String> getEndDeviceList(@RequestParam(value = "l", required = false) Integer l) {
        String endDeviceListXml = this.endDeviceImpl.getEndDeviceListHttp(l);
        LOGGER.info("the enddevice_list_val is " + endDeviceListXml);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        
        return new ResponseEntity<>(endDeviceListXml, headers, HttpStatus.OK);
    }

     @GetMapping(value = "/edev/{id}", produces = "application/sep+xml")
     public ResponseEntity<String> getEndDeviceByIdHttp(@PathVariable Long id) {
        String endDeviceXml = this.endDeviceImpl.getEndDeviceHttp(id);
        LOGGER.info("the edev_val is " + endDeviceXml);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        
        return new ResponseEntity<>(endDeviceXml, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/edev/{endDeviceID}/di", produces = "application/sep+xml")
     public ResponseEntity<String> getDeviceInformation(@PathVariable Long endDeviceID) {
        String deviceInfoXml = this.endDeviceImpl.getDeviceInformationHttp(endDeviceID);
        LOGGER.info("the dev_info_val is " + deviceInfoXml);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        
        return new ResponseEntity<>(deviceInfoXml, headers, HttpStatus.OK);
     }

     @PutMapping("/edev/{endDeviceId}/di")
     public String putDeviceInformation(@PathVariable Long endDeviceId, @RequestBody String payload){
        return endDeviceImpl.putDeviceInformationHttp(endDeviceId, payload);
     }

    public Map<String, Object>getEndDeviceById(@PathVariable Long id) 
    {   
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getEndDevice(id);
        
        return responseEntity.getBody(); 
    }

    @GetMapping(value = "/edev/{endDeviceID}/rg", produces = "application/sep+xml")
    public ResponseEntity<String> getEndDeviceRegistrationLink(@PathVariable Long endDeviceID) {
        String registeredEndDeviceXml = this.endDeviceImpl.getRegisteredEndDevice(endDeviceID);
        LOGGER.info("the registered_enddevice_val is " + registeredEndDeviceXml);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        
        return new ResponseEntity<>(registeredEndDeviceXml, headers, HttpStatus.OK);
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
 

     public EndDeviceEntity updateEndDevice( JSONObject jsonObject) {
        return null;
    }
    public void deleteEndDevice( String id) {

    }
    
}













