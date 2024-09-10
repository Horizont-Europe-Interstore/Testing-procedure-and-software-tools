package interstore.DERProgram;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import interstore.FunctionSetAssignments.FunctionSetAssignmentsEntity;

import java.util.Map;
import java.util.logging.Logger;

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
    
     

     /*  public static String EndDeviceListLinktest()
    { 
       expected payload is 
       "payload" : {
        derprogramLink :  }
   */

       public Map<String, Object> getDERPrograms(JSONObject payload) 
      {
        if(payload.has("derprogramLink"))
        {
            return getAllDERProgramDetails();
        }
        
        return null ; 
      }



    /*
     * public DERProgramEntity getDERProgramById(String id) throws NumberFormatException, NotFoundException {
        Long longId = Long.parseLong(id);
        ResponseEntity<Map<String, Object>> derProgramEntity = this.derProgramService.getDerProgramById(longId);
        return derProgramEntity;
    }
     */
    
    @GetMapping("/derp")
    public Map<String, Object> getAllDERProgramDetails() throws JSONException {
        ResponseEntity<Map<String, Object>> responseEntity = this.derProgramService.getAllDerPrograms();
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
