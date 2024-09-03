package interstore.DERProgram;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;

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
                addDERProgram(jsonObject);
                break;
            case "get":
                return getDERProgramByListLinks(jsonObject.get("payload"));
            case "put":
                updateDERProgram(jsonObject);
                break;
            case "delete":
                deleteDERProgram(jsonObject);
                break;

        }
        return "Operation completed successfully";
    }
    public Map<String, Object> addDERProgram( JSONObject jsonObject) throws NumberFormatException, JSONException, NotFoundException {
        {
            LOGGER.info("the received payload in the DER program Manager class  is " + jsonObject);
            DERProgramEntity derProgramEntity = this.derProgramService.createDerProgram(jsonObject);
            return Map.of("id", derProgramEntity.getId(), "primacy", derProgramEntity.getPrimacy());
            
        }
    }
    
    /*
     * public DERProgramEntity getDERProgramById(String id) throws NumberFormatException, NotFoundException {
        Long longId = Long.parseLong(id);
        ResponseEntity<Map<String, Object>> derProgramEntity = this.derProgramService.getDerProgramById(longId);
        return derProgramEntity;
    }
     */
    

    public String getDERProgramByListLinks(Object payload) throws JSONException {
        return null; 
       // return derProgramImpl.getDerProgramByListLinks(payload);
//        return derProgramImpl.getDerProgramLists(payload);

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
