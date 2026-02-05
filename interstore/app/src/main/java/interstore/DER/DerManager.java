package interstore.DER;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import interstore.DER.DERCapability.DERCapabilityEntity;
import interstore.DER.DERSettings.DERSettingsEntity;
import interstore.DER.DerPowerTests;
import interstore.XmlValidation.XmlValidationService;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
public class DerManager {

    private DerService derService;
    private static final Logger LOGGER = Logger.getLogger(DerManager.class.getName());
    private DerPowerTests derPowerGenerationTest;
    
    @Autowired
    private XmlValidationService xmlValidationService;

    public DerManager(DerService derService) {
        this.derService = derService;
        this.derPowerGenerationTest = new DerPowerTests();
    }

    

    // Internal method (called from NATS/App.java)
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
                return addDer(jsonObject);
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

    
    public Map<String, Object> addDer( JSONObject payload) throws NumberFormatException, JSONException, NotFoundException {
        if(payload.getJSONObject("payload").has("endDeviceID")){
            DerEntity derEntity = this.derService.createDerEntity(payload);
            LOGGER.info("the response from DER Entity get ID is " +  derEntity.getId()); 
            return Map.of("id", derEntity.getId(), "derLink", derEntity.getDerLink());
        }
        else
        {
            return Map.of("error", "endDeviceID not present in the payload");
        }
    }

    public Object getDer(JSONObject payload) throws JSONException
    {
       if(payload.has("endDeviceID") && payload.has("derID"))
       {
           Long endDeviceId = payload.getLong("endDeviceID");
           Long derId = payload.getLong("derID");
           LOGGER.info("the received payload in the DER program Manager for Get A DER is " +  payload);

           if (payload.has("powerGenerationTest") && payload.getBoolean("powerGenerationTest")) {
               return getDerCapabilityAndDerSettings(payload);
           }

           if (payload.has("derCapabilities")) {
               LOGGER.info("Getting DER Capability for endDeviceId: " + endDeviceId + ", derId: " + derId);
               return derService.getDerCapabilityHttp(endDeviceId, derId);
           }

           if (payload.has("derSettings")) {
               LOGGER.info("Getting DER Settings for endDeviceId: " + endDeviceId + ", derId: " + derId);
               return derService.getDerSettingsHttp(endDeviceId, derId);
           }

           ResponseEntity<Map<String, Object>> response = derService.getDer(derId, endDeviceId);
           return response.getBody();
       }
      return null ;
    }

    public Object getDerCapabilityAndDerSettings(JSONObject payload) throws JSONException
    {
        if(payload.has("endDeviceID") && payload.has("derID"))
        {
            Long endDeviceId = payload.getLong("endDeviceID");
            Long derId = payload.getLong("derID");
            LOGGER.info("Running Power Generation Test for endDeviceId: " + endDeviceId + ", derId: " + derId);

            String derPowerCapabilityResponse = derService.getDerCapabilityHttp(endDeviceId, derId);
            String derPowerSettingsResponse = derService.getDerSettingsHttp(endDeviceId, derId);
            
            Map<String, String> derPowerTests = Map.of(
                "derCapability", derPowerCapabilityResponse,
                "derSettings", derPowerSettingsResponse
            );
            
            return derPowerGenerationTest.powerGenerationTest(derPowerTests);
        }
        return null;
    }

    @GetMapping("/api/derpowergenerationtest/{endDeviceId}/{derId}")
    public Map<String, Object> derPowerGenerationTestHttp(@PathVariable Long endDeviceId, @PathVariable Long derId) throws JSONException {
        LOGGER.info("=== POWER GENERATION TEST ENDPOINT HIT ===");
        LOGGER.info("Running Power Generation Test for endDeviceId: " + endDeviceId + ", derId: " + derId);
        
        String derPowerCapabilityResponse = derService.getDerCapabilityHttp(endDeviceId, derId);
        String derPowerSettingsResponse = derService.getDerSettingsHttp(endDeviceId, derId);
        
        // Trigger XML validation for both requests
        if (xmlValidationService != null) {
            xmlValidationService.validateXml(
                "/edev/" + endDeviceId + "/der/" + derId + "/dercap",
                "GET",
                "",
                derPowerCapabilityResponse
            );
            xmlValidationService.validateXml(
                "/edev/" + endDeviceId + "/der/" + derId + "/derg",
                "GET",
                "",
                derPowerSettingsResponse
            );
        }
        
        Map<String, String> derPowerTests = Map.of(
            "derCapability", derPowerCapabilityResponse,
            "derSettings", derPowerSettingsResponse
        );
        
        return (Map<String, Object>) derPowerGenerationTest.powerGenerationTest(derPowerTests);
    }




    public Map<String, Object> updateDerSettings(JSONObject payload) throws JSONException, NumberFormatException, NotFoundException
    {

    if (payload.has("payload")) {
        JSONObject innerPayload = payload.getJSONObject("payload");

        if (innerPayload.has("endDeviceId") && innerPayload.has("derID")) {
            Long endDeviceId = innerPayload.getLong("endDeviceId");
            Long derId = innerPayload.getLong("derID");
            if (payload.has("powergeneration")) {
                ResponseEntity<Map<String, Object>> responseEntity = this.derService.updatePowerGenerationTest(endDeviceId, derId, innerPayload);
                return responseEntity.getBody();
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
                String responseEntity = derService.getDerHttp(endDeviceId, derId);
        
                LOGGER.info("the der val is " + responseEntity);
                request.setAttribute("responseXml", responseEntity);
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
    public Map<String, Object> getDerCapabilityDetailsHttp(@PathVariable Long endDeviceId, @PathVariable Long derId, HttpServletRequest request,
                                                HttpServletResponse response) throws JSONException {
        
        if (RequestContextHolder.getRequestAttributes() != null) {
            try{
                String responseEntity = this.derService.getDerCapabilityHttp( endDeviceId, derId);
        
                LOGGER.info("the Der Capability val is " + responseEntity);
                request.setAttribute("responseXml", responseEntity);
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
                LOGGER.severe("Error retrieving Der Capability value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        }
       
        return Map.of("error", "DER Capability endpoint must be called via HTTP");
        
    }
    
   

    @GetMapping("edev/{endDeviceId}/der/{derId}/derg")
    public Map<String, Object> getDerSettingsDetailsHttp(@PathVariable Long endDeviceId, @PathVariable Long derId, HttpServletRequest request,
                                                HttpServletResponse response) throws JSONException {
        
        if(RequestContextHolder.getRequestAttributes() != null){
            try{
                String responseEntity = this.derService.getDerSettingsHttp( endDeviceId, derId);
        
                LOGGER.info("the Der Settings val is " + responseEntity);
                request.setAttribute("responseXml", responseEntity);
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
                LOGGER.severe("Error retrieving Der Settings value: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        }
        
        return Map.of("error", "DER Settings endpoint must be called via HTTP");
    }
   
    

    @PutMapping("edev/{endDeviceId}/der/{derId}/derg")
    public Map<String, Object> updatePowerGenerationTestHttp(
            @PathVariable Long endDeviceId,
            @PathVariable Long derId,
            @RequestBody String payload,
            @RequestHeader(value = "Content-Type", required = false) String contentType)
            throws Exception {

        LOGGER.info("Received PUT request for DER Settings (derg). Content-Type: " + contentType);
        LOGGER.info("Raw payload: " + payload);

        // Parse both JSON and XML payloads
        JSONObject parsedPayload = PayloadParser.parseDERSettingsXml(payload, endDeviceId, derId);

        ResponseEntity<Map<String, Object>> responseEntity = this.derService.updatePowerGenerationTest(endDeviceId, derId, parsedPayload);
        return responseEntity.getBody();
    }

    @PutMapping("edev/{endDeviceId}/der/{derId}/dercap")
    public String updateDerCapabilityDetailsHttp(
            @PathVariable Long endDeviceId,
            @PathVariable Long derId,
            @RequestBody String payload,
            @RequestHeader(value = "Content-Type", required = false) String contentType)
            throws Exception {

        LOGGER.info("Received PUT request for DER Capability (dercap). Content-Type: " + contentType);
        LOGGER.info("Raw payload: " + payload);

        // Parse both JSON and XML payloads
        JSONObject parsedPayload = PayloadParser.parseDERCapabilityXml(payload, endDeviceId, derId);

        String responseEntity = this.derService.updateDERCapability(endDeviceId, derId, parsedPayload);
        return responseEntity;
    }

}


/* 
        else if(payload.getJSONObject("payload").has("derCapabilityLink")){
            DERCapabilityEntity derEntity = this.derService.createDerCapability( payload);
            LOGGER.info("the response from DER Capability get ID is " +  derEntity.getId()); 
            return Map.of("id", derEntity.getId(), "CurrentRMS", derEntity.getRtgMaxA(),
            "AmpereHour", derEntity.getRtgMaxAh());
        }
        else{
            DERSettingsEntity derEntity = this.derService.createDerSettings(payload);
                LOGGER.info("the response from DER Settings get ID is " +  derEntity.getId()); 
                return Map.of("id", derEntity.getId(), "setMaxA", derEntity.getSetMaxA(),
                "setMaxVA", derEntity.getSetMaxVA());
        }

        public Map<String, Object> getDerCapabilityDetails(Long derId, Long endDeviceId) throws JSONException {
        ResponseEntity<Map<String, Object>> responseEntity = this.derService.getDerCapability( endDeviceId, derId);
        return  responseEntity.getBody();
    }
        else{
            ResponseEntity<Map<String, Object>> responseEntity = this.derService.getDerCapability( endDeviceId, derId);
            return  responseEntity.getBody();
        }
             
    public Map<String, Object> getDerSettingsDetails(Long derId, Long endDeviceId) throws JSONException {
        ResponseEntity<Map<String, Object>> responseEntity = this.derService.getDerSettings(endDeviceId, derId);
        return  responseEntity.getBody();

    }

    else{
            ResponseEntity<Map<String, Object>> responseEntity = this.derService.getDerSettings(endDeviceId, derId);
            return  responseEntity.getBody();
        }
       if(payload.has("derSettings"))
             {  
                return getDerSettingsDetails(derId, endDeviceId);
             }
             else if (payload.has("derCapabilities")){
                return getDerCapabilityDetails( derId , endDeviceId );
             }

        */