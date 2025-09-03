package interstore.FunctionSetAssignments;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;
@RestController
public class FsaManager {
    FunctionSetAssignmentsService fsaService;
    private static final Logger LOGGER = Logger.getLogger(FsaManager.class.getName());
    public FsaManager(FunctionSetAssignmentsService fsaService) {
        this.fsaService = fsaService;
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
                return addFSA(jsonObject);
            case "get":
                return  getFSA(jsonObject);   
            case "put":
                updateFSA(jsonObject);
                break;
            case "delete":
                deleteFSA(jsonObject);
                break;

        }
        return "Operation completed successfully";
    }

    /*create a function set assignment from the payload 
     * the return shall be the id of the function set assignment 
     * and the mRID of the created function set assignment 
     *  expectedMap.put("mRID", "3E4F45");
        expectedMap.put("Version", "16726121139");
        expectedMap.put("id", "1");
     */
    public Map<String, Object> addFSA( JSONObject jsonObject) throws NumberFormatException, JSONException, NotFoundException {
        {  
            LOGGER.info("the received payload in the FSA Manager class  is " + jsonObject);
            FunctionSetAssignmentsEntity fsaEntity = this.fsaService.createFunctionsetAssignments(jsonObject);
            return Map.of("id", fsaEntity.getId(), "mRID", fsaEntity.getmRID(), "Version", fsaEntity.getVersion());
            
        }
    }
    /*  "payload" : {
     *  endDeviceID : 1 , both are string } this has to look for all 
     * function set assignemnts belongs to the particular end device 
     * condition 2 , "payload" : {
     *  endDeviceID : 1 
     *  fsaID : 1 , this has to look for the particular fsa
     * 
     * }
     *   "endDeviceID"
     */
    public Map<String, Object> getFSA(JSONObject payload) throws JSONException {
       
         if (payload.has("endDeviceID") && payload.has("fsaID"))
        {      
               Long endDeviceIdLong =  payload.getLong("endDeviceID");     
               Long fsaIdLong =  payload.getLong("fsaID");                                          
               LOGGER.info("endDeviceID in the FSA Manager" + endDeviceIdLong);
               LOGGER.info("fsaID in the FSA Manager" + fsaIdLong);

                return getFunctionSetAssignmentsDetails(endDeviceIdLong, fsaIdLong );
        }   

        else if(payload.has("endDeviceID"))
        {
            Long endDeviceIdLong =   payload.getLong("endDeviceID"); 
            LOGGER.info("endDeviceID in the FSA Manager" + endDeviceIdLong);
            return getEndDeviceById(endDeviceIdLong);
        }
        return null; 
    } 
    

    @GetMapping("/edev/{id}/fsa")
    public Map<String, Object>getFSAList(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) 
    {   
        // Case: called from HTTP
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = this.fsaService.getAllFunctionsetAssignmentsHttp(id);
        
                LOGGER.info("the fsaList_val is " + responseEntity);
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
                LOGGER.severe("Error retrieving FSAList value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        } 
        // Case: called from NATS (internal)
        else {
            ResponseEntity<Map<String, Object>> responseEntity = this.fsaService.getAllFunctionsetAssignments(id);
            return  responseEntity.getBody(); 
        }

        
    }


    public Map<String, Object>getEndDeviceById(@PathVariable Long id) 
    {   
        ResponseEntity<Map<String, Object>> responseEntity = this.fsaService.getAllFunctionsetAssignments(id);
        return  responseEntity.getBody(); 
    }
    
    @GetMapping("/edev/{endDeviceID}/fsa/{fsaID}")
     public Map<String, Object> getFunctionSetAssignmentsDetailsHttp(@PathVariable Long endDeviceID, @PathVariable Long fsaID, HttpServletRequest request, HttpServletResponse response)
      {
        // Case: called from HTTP
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = this.fsaService.getFunctionsetAssignmentsHttp(endDeviceID, fsaID);
        
                LOGGER.info("the fsa_val is " + responseEntity);
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
                LOGGER.severe("Error retrieving FSA value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        } 
        // Case: called from NATS (internal)
        else {
            ResponseEntity<Map<String, Object>> responseEntity = this.fsaService.getFunctionsetAssignments(endDeviceID, fsaID);
            return  responseEntity.getBody(); 
        }
     }

     public Map<String, Object> getFunctionSetAssignmentsDetails(@PathVariable Long endDeviceID, @PathVariable Long fsaID)
      {
        ResponseEntity<Map<String, Object>> responseEntity = this.fsaService.getFunctionsetAssignments(endDeviceID, fsaID);
        return responseEntity.getBody();
     }
    


    public void updateFSA( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
    public void deleteFSA( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
}

