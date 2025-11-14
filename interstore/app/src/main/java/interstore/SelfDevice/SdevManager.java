package interstore.SelfDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class SdevManager {
    private SelfDeviceService selfDeviceImpl;
    private static final Logger LOGGER = Logger.getLogger(SdevManager.class.getName());


    public SdevManager(SelfDeviceService selfDeviceImpl) {
        this.selfDeviceImpl = selfDeviceImpl;
    }

    public void chooseMethod_basedOnAction(String payload) throws JSONException{
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
                addSelfDevice(jsonObject);
                break;
            case "get":
                System.out.println(getSelfDevice(jsonObject));
                break;
            case "put":
                updateSelfDevice(jsonObject);
                break;
            case "delete":
                deleteSelfDevice(jsonObject.getString("id"));
                break;

        }
    }

    public void addSelfDevice( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }


    public SelfDeviceEntity getSelfDevice(JSONObject jsonObject) throws JSONException {
        SelfDeviceEntity data = selfDeviceImpl.getSelfDevice(jsonObject.getString("id"));
        return data;
    }

    public void updateSelfDevice( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
    public void deleteSelfDevice( String id) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
}
