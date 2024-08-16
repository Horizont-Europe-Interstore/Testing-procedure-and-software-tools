package interstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import interstore.EndDevice.EndDeviceDto;
import interstore.EndDevice.EndDeviceImpl;
import io.cucumber.core.gherkin.messages.internal.gherkin.internal.com.eclipsesource.json.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionSetAssignmentsTest {
    private static final Logger LOGGER = Logger.getLogger(FunctionSetAssignmentsTest.class.getName());
    private static String serviceName;
    static String listOfFunctionsetAssignemnts;
    static String createdFSA ; 
    static String fsaIds;
    static Object fsaInstances;
    static List<String> derpListLinks = new ArrayList<>();
    static String derProgramInstance;

    public FunctionSetAssignmentsTest(){

    }

    public static String getserviceName(){
        return serviceName;
    }
    public static void setServicename(String serviceName)
    {
        FunctionSetAssignmentsTest.serviceName = serviceName;
    }




   /* create a query to get all function set assignments  
   The method is to query for list of enddevices present in the server*/
      public static String getAllFsa(Long endDeviceID)
      { 
         
         Map<String, Object> attributes = new HashMap<>(); 
        attributes.put("servicename", getserviceName());
        attributes.put("action", "get");
        attributes.put("endDeviceID", endDeviceID);
         ObjectMapper objectMapper = new ObjectMapper();
         try {
             String postPayload = objectMapper.writeValueAsString(attributes);
             LOGGER.info("The payload for the get all FSA is " + postPayload); 
             return postPayload; 
             
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null; 
     }
  
    



    /* response for all function set assignemnt for an end device and
     using below setter it sets in variable 
     */
    public static String setAllFsa(String responseAllFSA) {
        LOGGER.info("The FSAList ID's for the EndDevice is "+ responseAllFSA);
        listOfFunctionsetAssignemnts = responseAllFSA;
        return responseAllFSA;
    }
    
    /*getter method to ge the response back to the front end part of
     * the cucmmber testing as response of the intail get all fsunction 
     * set assignemnts requests . 
     */
   public static String getAllFsa(){
        return listOfFunctionsetAssignemnts;
    }

    /* create new FSA the input this is called from the 
      method  createFunctionsetAssignments from app.java
      /* This method will create an end device in the server  */
  


    public static String createNewFunctionsetAssignments(JSONObject  PayLoad )
    {  
       
       try {
        String attributes = new JSONObject()
                                .put("servicename", getserviceName())
                                .put("action", "post")
                                .put("payload", (Object)PayLoad)
                                .toString();
           LOGGER.info(attributes);
           return attributes; 

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null ;
        
    }
   /* the setter will set the response of the newly created FSA 
    * in a memeber variable 
    */
    public static void setCreatedFunctionSetAssignment(String responseCreatedFSA)
    {
       createdFSA = responseCreatedFSA;
    }

    public static String getCreatedFunctionSetAssignment()
    {
        return createdFSA;
    }


    public Object getFSAInstance(Object responsePayload){
        LOGGER.info("The FSA instances for the EndDevice is " + responsePayload);
        fsaInstances = responsePayload;
        return responsePayload;
    }

//    public Map<String, String> getDerpMap(){
//        Map<String, String> derpMap = fsaImpl.getderpMap();
//        return derpMap;
//    }


    public static String getDerProgramInstance() {
        return derProgramInstance;
    }

    public static void setDerProgramInstance(String derProgramInstance) {
        FunctionSetAssignmentsTest.derProgramInstance = derProgramInstance;
    }

   
    public static String  getFSAListQuery(String regPin, Long id){
        String refClientPin = "111115";
        if (regPin.equals(refClientPin)) {
            LOGGER.info("PIN value is the same PIN value the REF-Client device has preregistered with pin: " + regPin);
            return queryServiceFSAList("get", id);
        } else {
            LOGGER.info("PIN value is not the same PIN value the REF-Client device has preregistered." + id);
            return queryServiceFSAList("get",  Integer.toUnsignedLong(0));
        }
    }
    public static List<Integer> getPin(String payload) throws JSONException{
        JSONObject jsonObject = new JSONObject(payload);
        JSONObject registeredEndDevice = jsonObject.getJSONObject("RegisteredEndDevice");
        int id = registeredEndDevice.getInt("id");
        int pin = registeredEndDevice.getInt("pin");
        List<Integer> values = new ArrayList<>();
//        String regex = "\"http://localhost/interstore/edev/(\\d+)/rg\":\"(\\d+)\"";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(regIDString);
//        if(matcher.find()) {
//            String edevID = matcher.group(1);
//            String regID = matcher.group(2);
//            values.add(edevID);
//            values.add(regID);
//        }
        values.add(id);
        values.add(pin);
        return values;
    }

//    public List<String> getDerpLinks(){
//        Map<String, String> derpMap = this.getDerpMap();
//        List<String> derplinks = new ArrayList<>();
//        for (String fsaLink : extractFsaLinks(fsaList)) {
//            if (derpMap.containsKey(fsaLink)) {
//                String derpValue = derpMap.get(fsaLink);
//                derplinks.add(derpValue);
//
//            } else {
//                LOGGER.info("No corresponding value found for link: " + fsaLink);
//            }
//        }
//        LOGGER.info("The DERProgramListLinks  for the EndDevice are: " + derplinks);
//        return derplinks;
//
//    }

    public static String getDERProgramInstance(String responsePayload){
        derProgramInstance = responsePayload;
        LOGGER.info("DERProgram Instance: " + derProgramInstance);
        return responsePayload;
    }
    public static void setDERPListLinks(){
        String val = fsaInstances.toString();
        Pattern pattern = Pattern.compile("DERProgramListLink=([^,}]+)");
        Matcher matcher = pattern.matcher(val);
        while (matcher.find()) {
            String link = matcher.group(1);
            derpListLinks.add(link);
        }
    }
    //    public static List<String> extractFsaLinks(String fsaListString) {
//        List<String> fsaLinks = new ArrayList<>();
//        Pattern pattern = Pattern.compile("\"(http://[^\\\"]+)\"");
//        Matcher matcher = pattern.matcher(fsaListString);
//
//        while (matcher.find()) {
//            fsaLinks.add(matcher.group(1));
//        }
//
//        return fsaLinks;
//    }
    public static String queryServiceFSAList( String action, Long regID)
    {
        EndDeviceImpl endDeviceImpl = ApplicationContextProvider.getApplicationContext().getBean(EndDeviceImpl.class);
        EndDeviceDto endDeviceDto = endDeviceImpl.getEndDeviceByRegistrationID(regID);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
        attributes.put("action", action);
        attributes.put("payload", endDeviceDto.getFunctionSetAssignmentsListLink());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String  getFSAQuery(String payload){

        return queryServiceFSA("get", payload);
    }
    public static String queryServiceFSA( String action, String payload)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
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
    public static String getDERProgramQuery(List<String> payload){
        return queryServiceDERProgram("get", payload);
    }
    public static String queryServiceDERProgram( String action, List<String> payload)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
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


}
