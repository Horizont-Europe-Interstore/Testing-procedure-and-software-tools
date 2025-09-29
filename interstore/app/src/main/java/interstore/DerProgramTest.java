package interstore;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import interstore.DERProgram.DERProgramManager;



@Component
public class DerProgramTest {
     private static final Logger LOGGER = Logger.getLogger(DerProgramTest.class.getName());
     static String listOfDerPrograms;
     static String createdDerProgram;
     static String derProgram;
     @Autowired
     private DERProgramManager derProgramManager;

   /* create a query to get all der programs 
     I have a question in my mind derProgramListLink
    should get all the der programs present in the server 
    but for that particular function set assignment . 
    One FSA ----> many der programs

    The listLink of the Der Program has to be associated with FSA , 
    so get the FSA ID correctly which corresponds for the DER program 
    and then get all the der programs for that FSA ID

   */
      public String getAllDerProgramRequest(Long fsaID)
      { 
         
         Map<String, Object> attributes = new HashMap<>(); 
        attributes.put("action", "get");
        attributes.put("fsaID", fsaID); 
        attributes.put("derprogramLink", "");
         ObjectMapper objectMapper = new ObjectMapper();
         try {
             String postPayload = objectMapper.writeValueAsString(attributes);
             LOGGER.info("The payload for the get all Der Program is " + postPayload);
             Object response = derProgramManager.chooseMethod_basedOnAction(postPayload);
             String jsonResponse = new ObjectMapper().writeValueAsString(response);
             setAllderPrograms(jsonResponse);
             return jsonResponse; 
             
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null; 
     }
   
      /* response for all der programs present in the server for an end device and
     using below setter it sets in variable 
     */
    public String setAllderPrograms(String responseAllFSA) {
        LOGGER.info("The FSAList ID's for the EndDevice is "+ responseAllFSA);
        listOfDerPrograms = responseAllFSA;
        return responseAllFSA;
    }
    
    /*getter method to get the response back to the front end part of
     * the cucmmber testing as response of the intail get all der programs
     * requests . 
     */
   public String getAllderPrograms(){
    return listOfDerPrograms;
}
    
  

   public String createNewDerProgram(JSONObject payload) 
   {
       try {
           String attributes = new JSONObject()
                   .put("action", "post")
                   .put("payload", (Object)payload)
                   .toString();
           LOGGER.info(attributes);
           Object response = derProgramManager.chooseMethod_basedOnAction(attributes);
           String jsonResponse = new ObjectMapper().writeValueAsString(response);
           setCreatedDerProgram(jsonResponse);
           return jsonResponse;

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null ;
   }

   public void  setCreatedDerProgram(String responseCreateDerProgram)
   { 
    createdDerProgram = responseCreateDerProgram;
       
   }
   
   public String getCreatedDerProgram()
   {
       return createdDerProgram;
   }    
   
    public String  getADerProgramRequest(Long fsaId, Long derId)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("action", "get");
        attributes.put("fsaID", fsaId);
        attributes.put("derID", derId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String postPayload = objectMapper.writeValueAsString(attributes);
            LOGGER.info("The payload for the get all Der Program is " + postPayload);
            Object response = derProgramManager.chooseMethod_basedOnAction(postPayload);
            String jsonResponse = new ObjectMapper().writeValueAsString(response);
            setADerprogram(jsonResponse);
            return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


   

   public static void setADerprogram(String responseDerProgram)
   {
    derProgram = responseDerProgram;
   } 

   public String getADerProgram()
   {
    return derProgram; 
   }

   }
   



