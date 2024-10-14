package interstore.DERControl;

import interstore.DERCurve.DERCurveEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.logging.Logger;

@RestController
public class DERControlManager {
    private static final Logger LOGGER = Logger.getLogger(DERControlManager.class.getName());

    DERControlService derControlService;

    public DERControlManager(DERControlService derControlService) {
        this.derControlService = derControlService;
    }

    public Object chooseMethod_basedOnAction(String payload) throws JSONException, NumberFormatException, ChangeSetPersister.NotFoundException {
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
                return addDERControl(jsonObject);
            case "get":
                return getDERControl(jsonObject);
            case "put":
                updateDERControl(jsonObject);
                break;
            case "delete":
                deleteDERControl(jsonObject);
                break;
        }
        return "Operation completed successfully";
    }

    public Map<String, Object> addDERControl(JSONObject payload) throws NumberFormatException, JSONException, ChangeSetPersister.NotFoundException {

        LOGGER.info("the received payload in the DER Control Manager class  is " +  payload);
        DERControlEntity derControlEntity = this.derControlService.createDERControl(payload);
        LOGGER.info("the response from DER Control get ID is " +  derControlEntity.getId());
        LOGGER.info("the response from DER Control derControlBase " +  derControlEntity.getDer_control_base());
        return Map.of("id", derControlEntity.getId(), "deviceCategory", derControlEntity.getDeviceCategory(), "derControlLink", derControlEntity.getDerControlLink(),"derControlBase", derControlEntity.getDer_control_base());

    }

    public Map<String, Object> getDERControl(JSONObject payload)
    {
        if(payload.has("derControlLink"))
        {
            return this.derControlService.
                    getDERControl(Long.parseLong(payload.getJSONObject("payload").getString("der_program_id")),
                            Long.parseLong(payload.getJSONObject("payload").getString("der_control_id")))
                    .getBody();
        }


        return getAllDERControlDetails(Long.parseLong(payload.getJSONObject("payload").getString("der_program_id")));
    }

    @GetMapping("/derp/{id}/derc")
    public Map<String, Object> getAllDERControlDetails(@PathVariable Long id){
        ResponseEntity<Map<String, Object>> responseEntity = this.derControlService.getAllDERControls(id);
        return  responseEntity.getBody();
    }

    public void updateDERControl( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }
    public void deleteDERControl( JSONObject jsonObject) {
        {
            LOGGER.info("Returned a 400 or 405 status message");
        }
    }

}
