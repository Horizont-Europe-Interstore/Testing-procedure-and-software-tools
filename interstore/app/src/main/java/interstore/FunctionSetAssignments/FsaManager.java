package interstore.FunctionSetAssignments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Logger;

public class FsaManager {
    FunctionSetAssignmentsService fsaService;
    private static final Logger LOGGER = Logger.getLogger(FsaManager.class.getName());
    public FsaManager(FunctionSetAssignmentsService fsaService) {
        this.fsaService = fsaService;
    }

    public Object chooseMethod_basedOnAction(String payload) throws JSONException{
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
                return getFSA(jsonObject.getString("payload"));
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
     */
    public Map<String, Object>  addFSA( JSONObject jsonObject) {
        {
            FunctionSetAssignmentsEntity fsaEntity = this.fsaService.createFunctionsetAssignments(jsonObject);
            LOGGER.info("id" + fsaEntity.getId());
            LOGGER.info("mRID" + fsaEntity.getmRID()); 
            return Map.of("id", fsaEntity.getId(), "mRID", fsaEntity.getmRID());
            
        }
    }
    /* This about get all functionsetassignments and 
       and individual fsa based on the fsa id passed 
     * not only fsa id but also the end device id 
     * condition 1 , "payload" : {
     *  endDeviceID : 1 , both are string } this has to look for all 
     * function set assignemnts belongs to the particular end device 
     * condition 2 , "payload" : {
     *  endDeviceID : 1 
     *  fsaID : 1 , this has to look for the particular fsa
     * }
     */
    public Map<String, Object> getFSA(String payload) {
        if(payload.contains("endDeviceID"))
        {
            return null ;//fsaService.getFunctionsetAssignmentsBasedOnEndDevice(payload);
        }
        else if(payload.contains("endDeviceID") & payload.contains("fsaID"))
        {
            return  null;//fsaService.getFunctionsetAssignments(payload);
        }
        return null;
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

/*
 *    public Object getFSA(String payload) {
        return fsaImpl.getFSA(payload);

    }
 * 
 */