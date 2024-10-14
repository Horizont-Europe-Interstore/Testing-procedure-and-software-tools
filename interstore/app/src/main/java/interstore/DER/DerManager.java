package interstore.DER;

import org.springframework.web.bind.annotation.RestController;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.logging.Logger;

@RestController
public class DerManager {

    private DerService derService;
    private static final Logger LOGGER = Logger.getLogger(DerManager.class.getName());

    public DerManager(DerService derService) {
        this.derService = derService;
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
                return addDerCapability(jsonObject);
            case "get":
                return getDer(jsonObject);
            case "put":
                return null ; //updateDERProgram(jsonObject);
            case "delete":
                //deleteDERProgram(jsonObject);
                break;

        }
        return "Operation completed successfully";
    }

    
    public Map<String, Object> addDerCapability( JSONObject payload) throws NumberFormatException, JSONException, NotFoundException {
        
            LOGGER.info("the received payload in the DER program Manager class  is " +  payload);
            if(payload.has("derSettings"))
            {
                
                DerEntity derEntity = this.derService.createDerSettings(payload);
                LOGGER.info("the response from DER Program get ID is " +  derEntity.getId()); 
                return Map.of("id", derEntity.getId(), "setMaxA", derEntity.getSetMaxA(),
                "setMaxVA", derEntity.getSetMaxVA());
            }
            else{
                DerEntity derEntity = this.derService.createDerCapability( payload);
                LOGGER.info("the response from DER Program get ID is " +  derEntity.getId()); 
                return Map.of("id", derEntity.getId(), "CurrentRMS", derEntity.getRtgMaxA(),
                "AmpereHour", derEntity.getRtgMaxAh());
            }

    }

    public Map<String, Object> getDer(JSONObject payload) throws JSONException 
    {
       if(payload.has("endDeviceId") && payload.has("derID"))
       {  
           Long endDeviceId = payload.getLong("endDeviceId");
           Long derId = payload.getLong("derID");
           LOGGER.info("the received payload in the DER program Manager for Get A DER Program is " +  payload);
           if(payload.has("derSettings"))
             {
                return getDerSettingsDetails(derId, endDeviceId);
             }
           return getDerCapabilityDetails( derId , endDeviceId );
       }
      return null ; 
    }

    @GetMapping("edev/{endDeviceId}/der/{derId}/dercap")
    public Map<String, Object> getDerCapabilityDetails(@PathVariable Long endDeviceId, @PathVariable Long derId) throws JSONException {
        ResponseEntity<Map<String, Object>> responseEntity = this.derService.getDerCapability( endDeviceId, derId);
        return  responseEntity.getBody();

    }
    
    @GetMapping("edev/{endDeviceId}/der/{derId}/derg")
    public Map<String, Object> getDerSettingsDetails(@PathVariable Long endDeviceId, @PathVariable Long derId) throws JSONException {
        ResponseEntity<Map<String, Object>> responseEntity = this.derService.getDerSettings(endDeviceId, derId);
        return  responseEntity.getBody();

    }


}
