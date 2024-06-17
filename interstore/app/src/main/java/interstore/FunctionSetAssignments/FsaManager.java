package interstore.FunctionSetAssignments;

import org.json.JSONObject;

import java.util.logging.Logger;

public class FsaManager {
    FsaImpl fsaImpl;
    private static final Logger LOGGER = Logger.getLogger(FsaManager.class.getName());
    public FsaManager(FsaImpl fsaImpl) {
        this.fsaImpl = fsaImpl;
    }

    public Object chooseMethod_basedOnAction(String payload){
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
                addFSA(jsonObject);
                break;
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
    public void addFSA( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }

    public Object getFSA(String payload) {
        return fsaImpl.getFSA(payload);

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
