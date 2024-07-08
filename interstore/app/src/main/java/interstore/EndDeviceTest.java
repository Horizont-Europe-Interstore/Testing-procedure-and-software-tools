package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.core.gherkin.messages.internal.gherkin.internal.com.eclipsesource.json.JsonObject;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class EndDeviceTest {
    private static final Logger LOGGER = Logger.getLogger(MessageFactory.class.getName()); 
     private static String endDeviceListLink; 
     private static String serviceName;
     private static String registrationLink; 
     private static String getAllEndDevices; 
     private static String sfdi ; 
     private static String endDeviceInstance; 
     private static String endDeviceID; 
     private static String registrationLinkEndPoint; 
     private static Long   registrationPin;
     private static String endDeviceEndPoint; 
     private static String tmpregistrationLinkId;
     private static String endDeviceLink;
     private static String registeredEndDeviceDetails; 


    public EndDeviceTest() {
       
    }
    
    public static void setServicename(String servicename)
    {
        serviceName = servicename;
    }

    public static String getserviceName(){     
        return serviceName; 
    } 
    /* setter and getter to test the end deviceListLink , the use of this to set the 
     * endDeviceListLink and this setter get the input from DevcieCapability Test 
     * which return the endDeviceListLink,getter will be called in the enddeviceListLink test 
     */
    public static void setEndDeviceListLink(String endDevicelistLink) {
        endDeviceListLink = endDevicelistLink;
       
    }

    public static String getEndDeviceListLink() {
        return endDeviceListLink;
    }
   
    public static void setEndDeviceEndPoint(String endDeviceEndPoint) {
        EndDeviceTest.endDeviceEndPoint = endDeviceEndPoint;
    }
    public static String getEndDeviceEndPoint() {
        return endDeviceEndPoint;
    } 

    public static void setendDeviceLink(String enddeviceLink) {
        endDeviceLink = enddeviceLink;
    }
    public static String getendDeviceLink() {
        return endDeviceLink;
    } 
    /* setter method to set the end point of registration since the enddevice has 
     * registrationlink as it's attribute , the endpoin tof this link has to be 
     * needed ti find out the registration link in variou tests related to enddevice
     * registration 
     */
    public static void setRegistrationLinkEndPoint(String registrationLinkEndpoint) {
        registrationLinkEndPoint = registrationLinkEndpoint;
    }
    public static String getRegistrationLinkEndPoint() {
        return registrationLinkEndPoint;
    }

    public static void setRegistrationPin(Long pin)
    {
        registrationPin = pin;
    }
    public static Long getRegistrationPin()
    {
        return registrationPin;
    } 




    /* The method is to query for list of enddevices present in the server*/
    public static String EndDeviceListLinktest()
    { 
       
       Map<String, Object> attributes = new HashMap<>(); 
      attributes.put("servicename", getserviceName());
      attributes.put("action", "get");
      attributes.put("payload", getEndDeviceListLink());
      attributes.put("endDeviceLink", getEndDeviceListLink());
       ObjectMapper objectMapper = new ObjectMapper();
       try {
           String postPayload = objectMapper.writeValueAsString(attributes); 
           return postPayload; 
           
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null; 
   }

   /* This method will create an end device in the server  */
   public static String createNewEndDevice(JSONObject PayLoad) throws JSONException  
   {  
        System.out.println("++++++++++++++++++++++++"+String.valueOf(PayLoad));
       String endDeviceendPoint = (String)PayLoad.get("endDeviceListLink");
       setEndDeviceEndPoint(endDeviceendPoint);
       String registrationLink = (String)PayLoad.get("registrationLink");
       setRegistrationLinkEndPoint(registrationLink); 
       String attributes = new JSONObject()
                                .put("servicename", getserviceName())
                                .put("action", "post")
                                .put("payload", (Object)PayLoad)
                                .toString();
       try {
           LOGGER.info(attributes);
           return attributes; 

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null ; 
   }
   
   public static String createEndDeviceRegistration(Long EndDeviceID, Long pin)
   {
       Map<String, Object> attributes = new HashMap<>();
       attributes.put("servicename", getserviceName());
       attributes.put("action", "post");
       attributes.put("endDeviceID", EndDeviceID);
       attributes.put("pin", pin); 
       attributes.put("payload", endDeviceID);
       ObjectMapper objectMapper = new ObjectMapper();
       try {
           String postPayload = objectMapper.writeValueAsString(attributes);
           System.out.println(postPayload);
           return postPayload;

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null ; 
   }


  
   /* All endevices present on the server will be stored in the getAllEndDevices variable
    *  this variable will be used in the getEndDeviceInstance test to get the end device instance
    *  from the server */
   public static void setEndDevices(String endDevices)
   {  
       getAllEndDevices = endDevices; 
   }


   public static String getEndDevices()
   {  
       return getAllEndDevices ; 
   }

    /* This method will get the end device instance from the server */ 
    public static String getEndDeviceInstancetest(Long endDeviceID)
    {  
        
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
        attributes.put("action", "get");
        //attributes.put("payload", endDeviceID);
        attributes.put("endDeviceID", endDeviceID); 
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("the instance that we are looking is " + postPayload);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }  
    /* the list of end devices will list all end devices present on 
     * the server among the end devices the user shall choose one 
     * end device , the end device is identified with sfdi and lfdi
     * the sfdi of the end device along with id of the end device which
     * is created by the database shall be available to user the below
     * code does this . This shall be reviewed while integrating with the 
     * database 
     */
     public static String setsfdi(String Sfdi) throws JSONException
     {
         try{
             LOGGER.info("the sfdi is here in form payload  " + Sfdi);
             Long requestedSfdi = Long.parseLong(Sfdi);
             String EndDevices = getEndDevices();
             JSONObject jsonObject = new JSONObject(EndDevices);
             JSONArray jsonArray = jsonObject.getJSONArray("endDevices");
             for(int i = 0 ; i < jsonArray.length() ; i++)
             {
                 JSONObject endDevice = jsonArray.getJSONObject(i);
                 Long sfdi = endDevice.getLong("sfdi");
                 if(sfdi == requestedSfdi)
                 {   String sfdiString = sfdi.toString();
                     setSfdi(sfdiString);
                     LOGGER.info("the sfdi is which is set and geeting from getter " + getSfdi());
                     Long endDeviceID = endDevice.getLong("id");
                     String endDeviceIDString = endDeviceID.toString();
                     setendDeviceID(endDeviceIDString);
                     String endDeviceLink = endDevice.getString("endDeviceLink");
                     setendDeviceLink(endDeviceLink);

                 }
             }
         }
         catch (Exception e){
             JSONObject myjsonObject = new JSONObject(Sfdi);
             JSONObject payload = myjsonObject.getJSONObject("payload");
             Long requestedSfdi = Long.parseLong(payload.getString("sfdi"));
             LOGGER.info("..............."+requestedSfdi);
             String EndDevices = getEndDevices();
             JSONObject jsonObject = new JSONObject(EndDevices);
             JSONArray jsonArray = jsonObject.getJSONArray("endDevices");
             for(int i = 0 ; i < jsonArray.length() ; i++)
             {
                 JSONObject endDevice = jsonArray.getJSONObject(i);
                 Long sfdi = endDevice.getLong("sfdi");
                 LOGGER.info("..............."+sfdi);
                 if(sfdi.equals(requestedSfdi))
                 {   LOGGER.info("insideeeeee...............");
                     String sfdiString = sfdi.toString();
                     setSfdi(sfdiString);
                     LOGGER.info("the sfdi is which is set and geeting from getter " + getSfdi());
                     Long endDeviceID = endDevice.getLong("id");
                     String endDeviceIDString = endDeviceID.toString();
                     setendDeviceID(endDeviceIDString);
                     String endDeviceLink = endDevice.getString("endDeviceLink");
                     setendDeviceLink(endDeviceLink);

                 }
             }
         }

        LOGGER.info("the sfdi is in the end device test class in form payload  " + Sfdi);
        Long requestedSfdi = Long.parseLong(Sfdi); 
       String EndDevices = getEndDevices(); 
       JSONObject jsonObject = new JSONObject(EndDevices);
       JSONArray jsonArray = jsonObject.getJSONArray("endDevices");
        for(int i = 0 ; i < jsonArray.length() ; i++)
        {
            JSONObject endDevice = jsonArray.getJSONObject(i);
            Long sfdi = endDevice.getLong("sfdi");
            if(sfdi == requestedSfdi)
            {   String sfdiString = sfdi.toString();
                setSfdi(sfdiString);
                LOGGER.info("the sfdi is which is set and geeting from getter " + getSfdi());
                Long endDeviceID = endDevice.getLong("id");
                String endDeviceIDString = endDeviceID.toString();
                setendDeviceID(endDeviceIDString);
                String endDeviceLink = endDevice.getString("endDeviceLink");
                setendDeviceLink(endDeviceLink);
            
            }
        }
        return null ; 
       }
       
       public static void setSfdi(String sfdiString)
       {
           sfdi = sfdiString;
       }
       public static String getSfdi()
       {
           return  sfdi;
       }
        public static void setendDeviceID(String endDeviceid)
       {
           endDeviceID = endDeviceid;
       }

       public static String getendDeviceID()
       {
           return endDeviceID;
       } 
        
    /* The setter and getter method to set the endDeviceInstance and this
     * getter method will be called where the particular end device of 
     * interest is needed */  
    public static void setEndDeviceInstance(String EndDevice)
       { 
        endDeviceInstance = EndDevice;  
         
       }
   
    public static String getEndDeviceInstance()
       {
           return endDeviceInstance;
       }
    /* The setter and getter method to set the registration link and this
     * getter method will be called where the particular end device with a 
     * registration link */
    public static void setRegistrationLink(String registrationlink) {
        registrationLink = registrationlink;
      }

    public static String getRegistrationLink() { 
        return registrationLink;
    }

    /*The method findout the registered end device link form the end device 
     * instance , then it call the setter to set the registration link
     * This method is directed from the message factory class where it 
     * identifies the corresponding test return methods to terminate the
     * test 
     */
   public static void getRegisteredEndDevice(String endDeviceResponsePayLoad)
    { 
       String endDevice = interstore.DeviceCapabilitytest.getEndDeviceEndPoint(); 
       String registeredEndDevice = getRegistrationLinkEndPoint();
       String patternString =  endDevice + "/(\\d+)" + registeredEndDevice ;  
       Pattern pattern = Pattern.compile(patternString);
       try {
        JSONObject jsonObject = new JSONObject(endDeviceResponsePayLoad);
        JSONObject endDeviceObject = jsonObject.getJSONObject("endDevice");
        Iterator<String> keys = endDeviceObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (endDeviceObject.get(key) instanceof String) {
                String value = endDeviceObject.getString(key);
                Matcher matcher = pattern.matcher(value);
                if (matcher.find()) {
                    LOGGER.info("Found matching Registration Link: " + value);
                    setRegistrationLink(value); 
                    
                }
            }
        }
    } catch (Exception e) {
        LOGGER.severe("Error processing the JSON payload: " + e.getMessage());
    }
}
  
public static void verifyRegisteredEndDevice(String registeredEndDevice) throws JSONException
{  
    JSONObject registeredEndDevicePayload = new JSONObject(registeredEndDevice);
    Long Pin = registeredEndDevicePayload.getLong("Pin");
    LOGGER.info("The registered end device pin is " + Pin); 
    String registrationLinkid =  registeredEndDevicePayload.optString("registrationLinkid", null);
    LOGGER.info("The registered end device registration link id is " + registrationLinkid);
    setRegistrationPin(Pin); 
    setEndDeviceregisteredwithId(registrationLinkid);
}

public static void setEndDeviceregisteredwithId(String registrationLinkid)
{
    tmpregistrationLinkId = registrationLinkid;
}
public static String getEndDeviceregisteredwithId()
{
    return tmpregistrationLinkId;
}

public static String findRegisteredEndDevice(Long endDeviceID, Long  registrationID)
{
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("servicename", getserviceName());
    attributes.put("action", "get");
    //attributes.put("payload",  getEndDeviceregisteredwithId());
    attributes.put("endDeviceID", endDeviceID);
    attributes.put("registrationID", registrationID);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        String postPayload = objectMapper.writeValueAsString(attributes);
        LOGGER.info("The registered end device payload is " + postPayload); 
        return postPayload;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null ;
}

public static void setregisteredEndDeviceDetails(String instanceofRegisteredEndDevice)
{
    LOGGER.info("Yaha aagaya........"+instanceofRegisteredEndDevice);
    registeredEndDeviceDetails = instanceofRegisteredEndDevice; 
}

public static String getregisteredEndDeviceDetails()
{
    return registeredEndDeviceDetails;
}

}



  

/*
 *  if(jsonArray.length() > 0)
       {
           JSONObject endDevice = jsonArray.getJSONObject(jsonArray.length() - 1);
            Long sfdi = endDevice.getLong("sfdi");
            String sfdiString = sfdi.toString();
            setSfdi(sfdiString);
            Long endDeviceID = endDevice.getLong("id");
            String endDeviceIDString = endDeviceID.toString();
            setendDeviceID(endDeviceIDString); 
            String endDeviceLink = endDevice.getString("endDeviceLink");
            setendDeviceLink(endDeviceLink); 
            
       }
 * 
 */