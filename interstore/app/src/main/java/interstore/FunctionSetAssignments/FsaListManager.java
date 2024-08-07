package interstore.FunctionSetAssignments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

/*
 * public class FsaListManager {
    FsaListImpl fsaListImpl;
    private static final Logger LOGGER = Logger.getLogger(FsaListManager.class.getName());
    public FsaListManager(FsaListImpl fsaListImpl) {
        this.fsaListImpl = fsaListImpl;
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
                addFSAList(jsonObject);
                break;
            case "get":
                return getFSAList(jsonObject.getString("payload"));

            case "put":
                updateFSAList(jsonObject);
                break;
            case "delete":
                deleteFSAList(jsonObject);
                break;

        }
        return "Operation completed successfully";
    }
    public void addFSAList( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }

    public Object getFSAList(String fsaListLink) {
        return fsaListImpl.getFSAList(fsaListLink);
    }


    public void updateFSAList( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
    public void deleteFSAList( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
}
 * 
 */




