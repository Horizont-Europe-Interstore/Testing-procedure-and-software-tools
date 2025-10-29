package interstore.DERProgram;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Logger;
@RestController
public class DERProgramManager {
    DERProgramService derProgramService;
    private static final Logger LOGGER = Logger.getLogger(DERProgramManager.class.getName());
    public DERProgramManager(DERProgramService derProgramService) {
        this.derProgramService = derProgramService;
    }

    public Object chooseMethod_basedOnAction(String payload) throws JSONException, NumberFormatException, NotFoundException{
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
                return addDERProgram(jsonObject);
            case "get":
                return getDERPrograms(jsonObject);
            case "put":
                updateDERProgram(jsonObject);
                break;
            case "delete":
                deleteDERProgram(jsonObject);
                break;

        }
        return "Operation completed successfully";
    }

    /*the received payload in the DER program Manager class  is {"payload":{"activeDERControlListLink":"actderc","mRID":"B01000000","defaultDERControlLink":"dderc","dERCurveListLink":"dc","derpLink":"derp","description":"der program fsa","fsaID":"1","primacy":"89","subscribable":"1",
    "version":"1","dERControlListLink":"derc"},"action":"post","servicename":"createDerprogrammanager"} */

    public Map<String, Object> addDERProgram( JSONObject payload) throws NumberFormatException, JSONException, NotFoundException {
        
            LOGGER.info("the received payload in the DER program Manager class  is " +  payload);
            DERProgramEntity derProgramEntity = this.derProgramService.createDerProgram( payload);
            LOGGER.info("the response from DER Program get ID is " +  derProgramEntity.getId()); 
            LOGGER.info("the response from DER Program primacy " +  derProgramEntity.getPrimacy());
            return Map.of("id", derProgramEntity.getId(), "primacy", derProgramEntity.getPrimacy());
            
        
    }
    
    
       public Map<String, Object> getDERPrograms(JSONObject payload) throws JSONException 
      {
         if(payload.has("derID"))
         {  
             Long derID = payload.getLong("derID");
             LOGGER.info("the received payload in the DER program Manager for Get A DER Program is " +  payload);
             return getDerProgramDetails(derID);
         }

        else{
            LOGGER.info("the received payload in the DER program Manager for Get All DER Program is " +  payload);
            return getAllDERProgramDetails();
        }
      }


    @GetMapping("/derp")
    public Map<String, Object> getAllDERProgramDetailsHttp( HttpServletRequest request, HttpServletResponse response) throws JSONException {
        // Case: called from HTTP
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = this.derProgramService.getAllDerProgramsHttp();
        
                LOGGER.info("the der_program_list_val is " + responseEntity);
                byte[] bytes = responseEntity.getBytes(StandardCharsets.UTF_8);
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/sep+xml;level=S1");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setContentLength(bytes.length);
                ServletOutputStream out = response.getOutputStream();
                out.write(bytes);
                out.flush();
            } catch(Exception e){
                LOGGER.severe("Error retrieving DERProgramList value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        } 
        // Case: called from NATS (internal)
        else {
            ResponseEntity<Map<String, Object>> responseEntity = this.derProgramService.getAllDerPrograms();
            return  responseEntity.getBody(); 
        }
    }

    public Map<String, Object> getAllDERProgramDetails() throws JSONException {
        ResponseEntity<Map<String, Object>> responseEntity = this.derProgramService.getAllDerPrograms();
        return  responseEntity.getBody(); 

    }

    @GetMapping("/derp/{derpID}")
    public Map<String, Object> getDerProgramDetailsHttp(@PathVariable Long derpID, HttpServletRequest request, HttpServletResponse response) throws JSONException {
        // Case: called from HTTP
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = this.derProgramService.getDerProgramHttp(derpID);
        
                LOGGER.info("the der_program_val is " + responseEntity);
                byte[] bytes = responseEntity.getBytes(StandardCharsets.UTF_8);
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/sep+xml;level=S1");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setContentLength(bytes.length);
                ServletOutputStream out = response.getOutputStream();
                out.write(bytes);
                out.flush();
            } catch(Exception e){
                LOGGER.severe("Error retrieving DERProgram value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        } 
        // Case: called from NATS (internal)
        else {
            ResponseEntity<Map<String, Object>> responseEntity = this.derProgramService.getDerProgram(derpID);
            return  responseEntity.getBody(); 
        }
    }

    public Map<String, Object> getDerProgramDetails(@PathVariable Long derpID) throws JSONException {
        ResponseEntity<Map<String, Object>> responseEntity = this.derProgramService.getDerProgram(derpID);
        return  responseEntity.getBody();

    }
    public void updateDERProgram( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
    public void deleteDERProgram( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
}







 /*
     * public DERProgramEntity getDERProgramById(String id) throws NumberFormatException, NotFoundException {
        Long longId = Long.parseLong(id);
        ResponseEntity<Map<String, Object>> derProgramEntity = this.derProgramService.getDerProgramById(longId);
        return derProgramEntity;
    }
     */

     
     /*  public static String EndDeviceListLinktest()
    { 
       expected payload is 
       "payload" : {
        derprogramLink :  }
   */