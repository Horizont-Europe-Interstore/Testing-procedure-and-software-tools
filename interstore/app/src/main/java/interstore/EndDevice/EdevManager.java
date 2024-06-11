package interstore.EndDevice;

import interstore.EndDeviceTest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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


    public  Map<String, Object> payLoadParser(JSONObject jsonObject)
    {   
       String[] extractedPayload = extractPayload(jsonObject);
       String payload = extractedPayload[1]; 
        try
        {
            Object response = splitPayload(payload);
            if(response instanceof String)
        {
            return this.getEndevices();
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
         
        }
    return null;
      
    }
    
    /*
     * http://localhost/edev/1/rg/1 , http://localhost/mycompany/api/edev/1/rg/1
     *  call the getter and compare it with the thing after '/' , create an 
     *   array from the url after the '/' get all the end points 
     *  iterate through it and find the one corresponding to the getter of enddevice 
     *  this you can get it from the device capablity becasue /edev is an attribute 
     */


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
    

    public String[] extractPayload( JSONObject jsonObject)
    { 
        String action = jsonObject.optString("action");
       String payload = jsonObject.optString("payload");
       return new String[]{action, payload}; 
    }
    
    public  Map<String, Object> identify_method(JSONObject jsonObject) throws InterruptedException
    {
        String payload = jsonObject.optString("payload");
        if(payload.contains("pin"))
        {
            return registerEndDevice(jsonObject);
        }
        return addEndDevice(jsonObject);
    }
   
    public Map<String, Object> addEndDevice( JSONObject jsonPayLoad) throws InterruptedException {
        EndDeviceDto endDeviceDto = this.endDeviceImpl.createEndDevice(jsonPayLoad);
        return this.endDeviceImpl.getEndDeviceID(endDeviceDto);  
       
    } 
    

    public Map<String, Object> registerEndDevice( JSONObject jsonPayLoad) throws InterruptedException {
        String endDevcieID = getEndDeviceInstance(jsonPayLoad);
        Long endDeviceId = Long.parseLong(endDevcieID);
        return  this.endDeviceImpl.registerEndDevice( jsonPayLoad, endDeviceId); 
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
    public Map<String, Object> getEndevices() {
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getAllEndDevices();
        return responseEntity.getBody();
    }

     @GetMapping("/edev/{id}")
    public Map<String, Object>getEndDeviceById(@PathVariable Long id) 
    {   
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getEndDevice(id);
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
    









     public EndDeviceDto updateEndDevice( JSONObject jsonObject) {
        return null;
    }
    public void deleteEndDevice( String id) {

    }
    
}













/*

  if("get".equals(action) && payload != null && payload.contains(getregistrationEndpoint()))
       {
       Pattern pattern = Pattern.compile("(?i)/([a-z]+)/([0-9]+)/([a-z]+)/([0-9]+)");
       Matcher matcher = pattern.matcher(payload );
       Long endDeviceId = Long.parseLong(matcher.group(2)); 
       LOGGER.info("the end device ID is " + endDeviceId);
       Long registeredEndDeviceId = Long.parseLong(matcher.group(4));
       LOGGER.info("the registered end device ID is " + registeredEndDeviceId);
        return this.getRegisteredEndDeviceDetails(endDeviceId, registeredEndDeviceId ); 
    } 








 public Object splitPayload(String payload)
    {  
        LOGGER.info("the payload in the split function is " + payload);
        String [] parts = payload.split("/");
        LOGGER.info("the parts are " + parts.length); 
        LOGGER.info("the first splitting " + parts[0] ) ;
        LOGGER.info("the second splitting " + parts[1] ) ;
        LOGGER.info("the third splitting " + parts[2] ) ;
        LOGGER.info("the fourth splitting " + parts[3] ) ;

        if(parts.length == 2)
        {
            LOGGER.info("the first splitting " + parts[1]);
            return parts[1];   // return a string 
        }  
       else if (parts.length == 3)
       {
         try {
            Long id = Long.parseLong(parts[2]);
            return id;              // return a Long 
        } catch(NumberFormatException e) {
            return "Invalid Numeric";
        }
         }
        else if(parts.length == 5)
        {
           try {
            Long endDeviceID = Long.parseLong(parts[2]);
            Long registeredId = Long.parseLong(parts[4]);
            return new String[] {String.valueOf(endDeviceID ), parts[3], String.valueOf(registeredId)}; // return arrylist 
        } catch(NumberFormatException e) { 
            return "Invalid Numeric";
           }
           
         }
         return null; 
    }


       if("get".equals(action) && (payload != null && payload.equals(getEndDeviceEndpoint())))
       { 
           return this.getEndevices();
       }
 
       if("get".equals(action) && payload != null && payload.contains(getEndDeviceEndpoint()))
       {
        LOGGER.info("the payload to get the end device instnae is " + payload);
       Pattern pattern = Pattern.compile("(?i)/([a-z]+)/([0-9]+)");
       Matcher matcher = pattern.matcher(payload );
       Long endDeviceId = Long.parseLong(matcher.group(2));
       LOGGER.info("the end device ID is " + endDeviceId);
        return this.getEndDeviceById(endDeviceId);
    } 
   




















@GetMapping("/edev/{id}/rg/{id}")
    public String getEndDeviceRegistrationLink(@PathVariable Long id, @PathVariable Long id2) {
        return this.endDeviceImpl.getEndDeviceRegistrationLink(id2);
    }
 
//if(payload.contains("pin")) // improve this if the key word pin changes code breaks , not acceptable 
             // {
               // return registerEndDevice(jsonObject);
              //}
              // return addEndDevice(jsonObject);


//  {"payload":{"registrationLink":"http://localhost/edev/1/rg","pin":"111111"},"action":"post","servicename":"enddeviceregistrationmanager"}
    //{"payload":{"registrationLink":"/rg","deviceCategory":"0","subscriptionLink":"/sub","deviceStatusLink":"/dstat","lfdi":"3E4F45","sfdi":"16726121139L","endDeviceListLink":"/edev","functionsetAssignmentLink":"/fsa"},"action":"post","servicename":"createnewenddevice"}





 ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = jsonPayLoad.toString();
        JSONObject copyJsonPayload;
        try {
            copyJsonPayload = new JSONObject(objectMapper.readValue(jsonString, Map.class));
        String action = jsonPayLoad.optString("action");
        String payload = jsonPayLoad.optString("payload");
        String enddeviceEndpoint = endDeviceTest.getEndDeviceListLink();
        String registrationLink = "/([^/]+)" + endDeviceTest.getRegistrationLinkEndPoint();
      //  LOGGER.info("registration link endpoint is that ...  " + registrationLink); // "/([^/]+)"
        LOGGER.info("the payload from the jsonObject is BA " +  copyJsonPayload ); 
        if("post".equals(action) && (payload != null && payload.contains( enddeviceEndpoint))) 
           {
            EndDeviceDto endDeviceDto = this.endDeviceImpl.createEndDevice(copyJsonPayload);
            LOGGER.info("end Device is ... L" +  this.endDeviceImpl.getEndDeviceID(endDeviceDto));
            return this.endDeviceImpl.getEndDeviceID(endDeviceDto);      
           }
           
        else if("post".equals(action) &&( payload != null && payload.contains(registrationLink ))) {
            LOGGER.info("the payload from the jsonObject is ....." +  payload );
            try {
                String endDevcieID = getEndDeviceInstance(payload);
                Long endDeviceId = Long.parseLong(endDevcieID);
                LOGGER.info("the endDevice ID is ..." +  endDeviceId);
                this.endDeviceImpl.registerEndDevice(payload, endDeviceId);
            } catch (Exception e) {
                LOGGER.info("the exception is " + e);
            }
        }
         } catch (JsonMappingException e) {
          
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return null ;     
             








       // String regex = "(?i).*\\/[a-z]+\\/(\\d+)\\/[a-z]+";
        //String endDevcieID = getEndDeviceInstance(payload);
        //try{
           // if ("post".equals(action) && payload != null && payload.matches(regex ))
       // {   
            
            // ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getEndDevice(endDeviceId);
            //return endDeviceImpl.getEndDeviceID(endDeviceDto)
        //}
       // } catch (Exception e) {
           // LOGGER.info("the exception is " + e);
        //} 
        
           
           // LOGGER.info("the response after creatign th end device is " + this.endDeviceImpl.getEndDeviceID(endDeviceDto));
           





//LOGGER.info("the payload received in the parser is " + jsonObject); 
      // String action = jsonObject.optString("action");
       //String payload = jsonObject.optString("payload"); 
      //String payload = jsonObject.optString("payload"); 
 
@GetMapping("/{type}")
    public Map<String, Object> getDevices(@PathVariable String type) {
        // Assuming endDeviceImpl has a method to fetch devices based on a dynamic type or endpoint
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getAllEndDevices();
        return responseEntity.getBody();
    }





@GetMapping("/edev")  
 public Map<String, Object> getEndevices()
 {    
      ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getAllEndDevices();
      return responseEntity.getBody(); 
    
 }



  
     @GetMapping("/edev/{id}")
    public Map<String, Object>getEndDeviceById(@PathVariable Long id) 
    {   
        LOGGER.info("the id is " + id);
        ResponseEntity<Map<String, Object>> responseEntity = this.endDeviceImpl.getEndDevice(id);
        return responseEntity.getBody(); 
    }












 // String payload = jsonObject.toString();
        //Pattern  endDevciepattern =  Pattern.compile("edev/(\\d+)/");
       // Pattern registerdendDevicepattern = Pattern.compile("edev/(\\d+)/rg");
        //Matcher endDeviceMatcher = endDevciepattern.matcher(payload);
       // Matcher registeredEndDeviceMatcher = registerdendDevicepattern.matcher(payload);

        if (payload.contains("/edev"))
        {

            this.endDeviceImpl.getEndDeviceListLinks();

        }

        else if  (payload.contains("/edev") && payload.contains("/rg")) {

        }

        else if(registeredEndDeviceMatcher.find())
        {
            String endDeviceID = registeredEndDeviceMatcher.group(1);











 // return this.endDeviceImpl.getAllLinks(endDeviceDto);  // there should be a different method for gettall devices in tis class , has to impliment it . 
        //this.endDeviceImpl.createEndDevice(endDeviceDto); 
    }  

   //LOGGER.info("the json message is " + jsonObject);
           // LOGGER.info("the endDeviceImpl is " + this.endDeviceImpl);
           // EndDeviceDto endDeviceDto = new EndDeviceDto();
 * public Map<String, Object> getEndDevice( JSONObject jsonObject) {
        String EndDevicelink = jsonObject.toString(); 
        Pattern  endDevciepattern =  Pattern.compile("edev/(\\d+)/");
        Pattern registerdendDevicepattern = Pattern.compile("edev/(\\d+)/rg");
        Matcher endDeviceMatcher = endDevciepattern.matcher(EndDevicelink);
        Matcher registeredEndDeviceMatcher = registerdendDevicepattern.matcher(EndDevicelink);
       
        if (EndDevicelink .contains("/endDeviceListLink"))
        {
           
           Map<String, Object> endDeviceListLinkMap = this.endDeviceImpl.getEndDeviceListLinks();
           return endDeviceListLinkMap ; 
          
        }
   
        else if  (EndDevicelink.contains("/edev") && EndDevicelink.contains("/rg")) {
           
         }
 
         else if(registeredEndDeviceMatcher.find())
          {   
            String id = registeredEndDeviceMatcher.group(1); 
            LOGGER.info("The registeration link for end device is now passing id  " + id);
            Map<String, Object> registeredEndDeviceMap = this.endDeviceImpl.getRegisteredEndDeviceById(id);
            return registeredEndDeviceMap ;
             
          }
        else if(endDeviceMatcher.find())
        {
            String id = EndDevicelink.substring(EndDevicelink.lastIndexOf('/') );
            Map<String, Object> endDeviceMap = this.endDeviceImpl.getEndDeviceById(id);
            return endDeviceMap ; 
       }
       
        Map<String, Object> endDeviceAttributes = this.endDeviceImpl.getEndDevice(EndDevicelink); 
        LOGGER.info("The endDeviceDto is  here with attributes " + endDeviceAttributes);
        return endDeviceAttributes ;  
       //return null;  // change it to map later with a default value with invalid operation 
    }
 *  
 * 
 * 
 * 
 * 
 * 
 * 
 */