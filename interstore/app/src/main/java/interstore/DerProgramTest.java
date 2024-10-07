package interstore;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;




public class DerProgramTest {
     private static final Logger LOGGER = Logger.getLogger(DerProgramTest.class.getName());
     private static String serviceName;
     static String listOfDerPrograms;
     static String createdDerProgram;
     static String derProgram;

 

     public static String getserviceName(){
        return serviceName;
    }
    public static void setServicename(String serviceName)
    {
        DerProgramTest.serviceName = serviceName;
    }

   /* create a query to get all der programs 
     I have a question in my mind derProgramListLink
    should get all the der programs present in the server 
    but for that particular function set assignment . 
    One FSA ----> many der programs

    The listLink of the Der Program has to be associated with FSA , 
    so get the FSA ID correctly which corresponds for the DER program 
    and then get all the der programs for that FSA ID

   */
      public static String getAllDerProgramRequest(Long fsaID)
      { 
         
         Map<String, Object> attributes = new HashMap<>(); 
        attributes.put("servicename", getserviceName());
        attributes.put("action", "get");
        attributes.put("fsaID", fsaID); 
        attributes.put("derprogramLink", "");
         ObjectMapper objectMapper = new ObjectMapper();
         try {
             String postPayload = objectMapper.writeValueAsString(attributes);
             LOGGER.info("The payload for the get all Der Program is " + postPayload); 
             return postPayload; 
             
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null; 
     }
   
      /* response for all der programs present in the server for an end device and
     using below setter it sets in variable 
     */
    public static String setAllderPrograms(String responseAllFSA) {
        LOGGER.info("The FSAList ID's for the EndDevice is "+ responseAllFSA);
        listOfDerPrograms = responseAllFSA;
        return responseAllFSA;
    }
    
    /*getter method to get the response back to the front end part of
     * the cucmmber testing as response of the intail get all der programs
     * requests . 
     */
   public static String getAllderPrograms(){
    return listOfDerPrograms;
}
    
  

   public static String createNewDerProgram(JSONObject payload) 
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

   public static void  setCreatedDerProgram(String responseCreateDerProgram)
   { 
    createdDerProgram = responseCreateDerProgram;
       
   }
   
    public static String  getADerProgramRequest(Long fsaId, Long derId)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("servicename", getserviceName());
        attributes.put("action", "get");
        attributes.put("fsaID", fsaId);
        attributes.put("derID", derId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("The payload for the get all Der Program is " + postPayload);
            return postPayload;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


   public static String getCreatedDerProgram()
   {
       return createdDerProgram;
   }    
   

   public static void setADerprogram(String responseDerProgram)
   {
    derProgram = responseDerProgram;
   } 

   public static String getADerProgram()
   {
    return derProgram; 
   }

   }
   



