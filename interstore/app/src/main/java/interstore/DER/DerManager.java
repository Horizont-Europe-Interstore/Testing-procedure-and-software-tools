package interstore.DER;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.nio.charset.StandardCharsets;
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
        LOGGER.info("the received payload in DER .....power " +  payload);
        JSONObject jsonObject = new JSONObject(payload);
        if(!jsonObject.has("action"))
        {
            throw new IllegalArgumentException("action cannot be null or empty");
        }
        String action = jsonObject.getString("action");
        switch(action){
            case "post":
                return addDerCapability(jsonObject);
            case "get":
                return getDer(jsonObject);
            case "put":
                LOGGER.info("the received payload inside switch case DER .....Power " +  payload);
                return updateDerSettings(jsonObject);
            case "delete":
                break;

        }
        return "Operation completed successfully";
    }

    
    public Map<String, Object> addDerCapability( JSONObject payload) throws NumberFormatException, JSONException, NotFoundException {
        
            LOGGER.info("the received payload in the DER Manager class  is " +  payload);
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
             else if (payload.has("derCapabilities")){
                return getDerCapabilityDetails( derId , endDeviceId );
             }
             ResponseEntity<Map<String, Object>> response = derService.getDer(derId, endDeviceId);
             return response.getBody();
       }
      return null ; 
    }

    
    public Map<String, Object> updateDerSettings(JSONObject payload) throws JSONException, NumberFormatException, NotFoundException
    {
    
    if (payload.has("payload")) {
        JSONObject innerPayload = payload.getJSONObject("payload");
        
        if (innerPayload.has("endDeviceId") && innerPayload.has("derID")) {
            Long endDeviceId = innerPayload.getLong("endDeviceId");
            Long derId = innerPayload.getLong("derID");
            if (payload.has("powergeneration")) {
                return updatePowerGenerationTest(endDeviceId, derId, innerPayload);
            }
        }
    }

    return null;
    }

    @GetMapping("edev/{endDeviceId}/der")
    public Map<String, Object> getAllDerHttp(@PathVariable Long endDeviceId, HttpServletRequest request,
                                                HttpServletResponse response) throws JSONException {
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = derService.getAllDERsHttp(endDeviceId);
        
                LOGGER.info("the der_list val is " + responseEntity);
                byte[] bytes = responseEntity.getBytes(StandardCharsets.UTF_8);
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/sep+xml;level=S1");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setContentLength(bytes.length);
                ServletOutputStream out = response.getOutputStream();
                out.write(bytes);
                out.flush();
            } catch(Exception e){
                LOGGER.severe("Error retrieving DERList value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        }
        return null; 

    }
   
    @GetMapping("edev/{endDeviceId}/der/{derId}")
    public Map<String, Object> getADerHttp(@PathVariable Long endDeviceId, @PathVariable Long derId, HttpServletRequest request,
                                                HttpServletResponse response) throws JSONException {
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = derService.getDerHttp(derId, endDeviceId);
        
                LOGGER.info("the der val is " + responseEntity);
                byte[] bytes = responseEntity.getBytes(StandardCharsets.UTF_8);
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/sep+xml;level=S1");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Connection", "keep-alive");
                response.setContentLength(bytes.length);
                ServletOutputStream out = response.getOutputStream();
                out.write(bytes);
                out.flush();
            } catch(Exception e){
                LOGGER.severe("Error retrieving Der value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        } 
        // Case: called internally
        else {
            ResponseEntity<Map<String, Object>> responseEntity = derService.getDer(derId, endDeviceId);
             return responseEntity.getBody();
        }

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
   
    @PutMapping("edev/{endDeviceId}/der/{derId}/derg")
    public Map<String, Object> updatePowerGenerationTest(@PathVariable Long endDeviceId, @PathVariable Long derId , JSONObject payload) throws JSONException, NotFoundException {
        ResponseEntity<Map<String, Object>> responseEntity = this.derService.updatePowerGenerationTest(endDeviceId, derId, payload);
        return  responseEntity.getBody();

    }

    @PutMapping("edev/{endDeviceId}/der/{derId}/dercap")
    public Map<String, Object> updateDerCapabilityDetails(@PathVariable Long endDeviceId, @PathVariable Long derId , JSONObject payload) throws JSONException, NotFoundException {
        ResponseEntity<Map<String, Object>> responseEntity = this.derService.updatePowerGenerationTest(endDeviceId, derId, payload);
        return  responseEntity.getBody();

    }

}


/*
 *  public Map<String, Object> updateDerSettings(JSONObject payload) throws JSONException, NumberFormatException, NotFoundException
    {    
        LOGGER.info("the received payload of DER Settings .. if " +  payload);
        if(payload.has("endDeviceId") && payload.has("derID"))
         { 
          
            Long endDeviceId = payload.getLong("endDeviceId");
            Long derId = payload.getLong("derID");
            LOGGER.info("the received payload in the DER program Manager for Update A DER Program is outside inner if " +  payload);
            if(payload.has("powergeneration"))
            {    
                LOGGER.info("the received payload in the DER program Manager for Update A DER Settings is " +  payload);
                return updatePowerGenerationTest( endDeviceId,  derId ,payload);
            }
         }
          
       return null; 
    }
 * 
 * 
 */