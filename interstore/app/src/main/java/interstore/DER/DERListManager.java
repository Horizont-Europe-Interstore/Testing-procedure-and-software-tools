package interstore.DER;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class DERListManager {

    private DERListImpl derListImpl;
    private static final Logger LOGGER = Logger.getLogger(DERListManager.class.getName());

    public DERListManager(DERListImpl derListImpl) {
        this.derListImpl = derListImpl;
    }

    public String chooseMethod_basedOnAction(String payload) throws InterruptedException, JSONException {
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
                return null;
            case "get":
                return getDERList(jsonObject.getString("payload"));
            case "put":
//                updateEndDevice(jsonObject);
                return null;  // change it to map later
            case "delete":
//                deleteEndDevice(jsonObject.getString("id"));
                return null;  // change it to map later

        }
        return null;  // change it to map later with a default value with invalid operation
    }

    public String getDERList(String DERListLink){
        return this.derListImpl.getDERList(DERListLink);
    }
}
