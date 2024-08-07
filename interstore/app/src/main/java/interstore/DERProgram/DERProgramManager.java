package interstore.DERProgram;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class DERProgramManager {
    DERProgramImpl derProgramImpl;
    private static final Logger LOGGER = Logger.getLogger(DERProgramManager.class.getName());
    public DERProgramManager(DERProgramImpl derProgramImpl) {
        this.derProgramImpl = derProgramImpl;
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
    public void addDERProgram( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }

    public String getDERProgramByListLinks(Object payload) throws JSONException {
        return derProgramImpl.getDerProgramByListLinks(payload);
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
