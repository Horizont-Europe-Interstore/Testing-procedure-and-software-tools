package interstore.DER;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import interstore.DER.DERCapability.DERCapabilityEntity;
import interstore.DER.DERSettings.DERSettingsEntity;
import interstore.DER.DerPowerTests;
import interstore.DER.DerUtils;
import interstore.XmlValidation.XmlValidationService;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;




@RestController
public class DerManager {

    private DerService derService;
    private static final Logger LOGGER = Logger.getLogger(DerManager.class.getName());
    private DerPowerTests derPowerGenerationTest;
    private DerUtils derUtil;

    
    @Autowired
    private XmlValidationService xmlValidationService;

    public DerManager(DerService derService) {
        this.derService = derService;
        this.derPowerGenerationTest = new DerPowerTests();
        this.derUtil = new DerUtils();
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
            case "powergeneration":
                 return powerGenerationTest(jsonObject);   
            case "reactivepower":
                 return reactivePowerTest(jsonObject);
            case "delete":
                break;

        }
        return "Operation completed successfully";
    }

    
    public Object powerGenerationTest(JSONObject payload) throws JSONException
    {
        
       if (payload.has("payload")) payload = payload.getJSONObject("payload");
       if(payload.has("endDeviceID") && payload.has("derID")) {
           Long endDeviceId = payload.getLong("endDeviceID");
           Long derId = payload.getLong("derID");
           
               String xmlDerCapability = derService.getDerCapabilityHttp(endDeviceId, derId);
               String xmlDerSettings = derService.getDerSettingsHttp(endDeviceId, derId);
               
               Map<String, String> derPowerTests = Map.of(
                       "derCapability", xmlDerCapability,
                       "derSettings", xmlDerSettings
                   );
                Map<String, Object> derPowerTestResults = derUtil.PowerGenerationHandler(derPowerTests);
                
                // Build combined XML for validation
                @SuppressWarnings("unchecked")
                Map<String, String> modeValidation = (Map<String, String>) derPowerTestResults.get("modeValidation");
                String combinedXml = derUtil.buildPowerGenerationXml(xmlDerCapability, xmlDerSettings, modeValidation);
                
                LOGGER.info("xmlValidationService is null: " + (xmlValidationService == null));
                LOGGER.info("Combined XML for validation: " + combinedXml);
                
                // Trigger XML validation
                if (xmlValidationService != null) {
                    LOGGER.info("Triggering XML validation for powergeneration");
                    xmlValidationService.validateXml(
                        "/edev/" + endDeviceId + "/der/" + derId + "/powergeneration",
                        "GET",
                        "",
                        combinedXml
                    );
                } else {
                    LOGGER.warning("xmlValidationService is null, skipping validation");
                }
                
                return derPowerTestResults;
           }
           return null;
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
       if (payload.has("payload")) payload = payload.getJSONObject("payload");
       if(payload.has("endDeviceID") && payload.has("derID")) {
           Long endDeviceId = payload.getLong("endDeviceID");
           Long derId = payload.getLong("derID");
           if (payload.has("derCapabilities")) {
               String xml = derService.getDerCapabilityHttp(endDeviceId, derId);
               return Map.of("xml", xml != null ? xml : "");
           }
           if (payload.has("derSettings")) {
               String xml = derService.getDerSettingsHttp(endDeviceId, derId);
               return Map.of("xml", xml != null ? xml : "");
           }
           String xml = derService.getDerHttp(endDeviceId, derId);
           return Map.of("xml", xml != null ? xml : "");
       }
       return null;
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

    @GetMapping(value = "edev/{endDeviceId}/der", produces = "application/sep+xml")
    public ResponseEntity<String> getAllDerHttp(@PathVariable Long endDeviceId) {
        String derListXml = derService.getAllDERsHttp(endDeviceId);
        LOGGER.info("the der_list val is " + derListXml);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        
        return new ResponseEntity<>(derListXml, headers, HttpStatus.OK);
    }
   
    @GetMapping(value = "edev/{endDeviceId}/der/{derId}", produces = "application/sep+xml")
    public ResponseEntity<String> getADerHttp(@PathVariable Long endDeviceId, @PathVariable Long derId) {
        String derXml = derService.getDerHttp(endDeviceId, derId);
        LOGGER.info("the der val is " + derXml);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        
        return new ResponseEntity<>(derXml, headers, HttpStatus.OK);
    } 

    @GetMapping(value = "edev/{endDeviceId}/der/{derId}/dercap", produces = "application/sep+xml")
    public ResponseEntity<String> getDerCapabilityDetailsHttp(@PathVariable Long endDeviceId, @PathVariable Long derId) {
        String derCapabilityXml = this.derService.getDerCapabilityHttp(endDeviceId, derId);
        LOGGER.info("the Der Capability val is " + derCapabilityXml);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        
        return new ResponseEntity<>(derCapabilityXml, headers, HttpStatus.OK);
    }
    
   

    @GetMapping(value = "edev/{endDeviceId}/der/{derId}/derg", produces = "application/sep+xml")
    public ResponseEntity<String> getDerSettingsDetailsHttp(@PathVariable Long endDeviceId, @PathVariable Long derId) {
        String derSettingsXml = this.derService.getDerSettingsHttp(endDeviceId, derId);
        LOGGER.info("the Der Settings val is " + derSettingsXml);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        
        return new ResponseEntity<>(derSettingsXml, headers, HttpStatus.OK);
    }
   
    @GetMapping(value = "edev/{endDeviceId}/der/{derId}/ders", produces = "application/sep+xml")
    public ResponseEntity<String> getDerStatusDetailsHttp(@PathVariable Long endDeviceId, @PathVariable Long derId) {
        String derStatusXml = this.derService.getDerStatusHttp(endDeviceId, derId);
        LOGGER.info("the Der Status val is " + derStatusXml);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        return new ResponseEntity<>(derStatusXml, headers, HttpStatus.OK);
     
    }
    
    @GetMapping(value = "edev/{endDeviceId}/der/{derId}/dera", produces = "application/sep+xml" )
    public ResponseEntity<String>getDerAvaialbilityDetailsHttp(@PathVariable Long endDeviceId, @PathVariable Long derId)
    {
        String derAvailabilityXml = this.derService.getDerAvailabilityHttp(endDeviceId, derId);
        LOGGER.info("the Der Availability val is " + derAvailabilityXml);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sep+xml;level=S1");
        headers.set("Cache-Control", "no-cache");
        return new ResponseEntity<>(derAvailabilityXml, headers, HttpStatus.OK);
    }

    @PutMapping("edev/{endDeviceId}/der/{derId}/derg")
    public String updatePowerGenerationTestHttp(
            @PathVariable Long endDeviceId,
            @PathVariable Long derId,
            @RequestBody String payload,
            @RequestHeader(value = "Content-Type", required = false) String contentType)
            throws Exception {

        LOGGER.info("Received PUT request for DER Settings (derg). Content-Type: " + contentType);
        LOGGER.info("Raw payload: " + payload);

        JSONObject parsedPayload = PayloadParser.parseDERSettingsXml(payload, endDeviceId, derId);
        ResponseEntity<Map<String, Object>> responseEntity = this.derService.updatePowerGenerationTest(endDeviceId, derId, parsedPayload);
        
        return this.derService.getDerSettingsHttp(endDeviceId, derId);
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

    @PutMapping("edev/{endDeviceId}/der/{derId}/ders")
    public String updateDerStatus( @PathVariable Long endDeviceId,
    @PathVariable Long derId,
    @RequestBody String payload,
    @RequestHeader(value = "Content-Type", required = false) String contentType)
    throws Exception {

        LOGGER.info("Received PUT request for DER Status (ders). Content-Type: " + contentType);
        LOGGER.info("Raw payload: " + payload);
    JSONObject parsedPayload = PayloadParser.parseDERStatusXml(payload, endDeviceId, derId);
    String responseEntity = this.derService.updateDERStatus(endDeviceId, derId, parsedPayload); 
    return responseEntity;
    }

    @PutMapping("edev/{endDeviceId}/der/{derId}/dera")
    public String updateDerAvailability(@PathVariable Long endDeviceId,
    @PathVariable Long derId,
    @RequestBody String payload,
    @RequestHeader(value = "Content-Type", required = false) String contentType)
    throws Exception {
        LOGGER.info("Received PUT request for DER Availability (dera). Content-Type: " + contentType);
        LOGGER.info("Raw payload: " + payload);
        JSONObject parsedPayload = PayloadParser.parseDERAvailabilityXml(payload, endDeviceId, derId);
        String responseEntity = this.derService.updateDERAvailability(endDeviceId, derId, parsedPayload);
        return responseEntity;
    }
   
    public Object reactivePowerTest(JSONObject payload) throws JSONException
    {
        
       if (payload.has("payload")) payload = payload.getJSONObject("payload");
       if(payload.has("endDeviceID") && payload.has("derID")) {
           Long endDeviceId = payload.getLong("endDeviceID");
           Long derId = payload.getLong("derID");
           
               String xmlDerCapability = derService.getDerCapabilityHttp(endDeviceId, derId);
               String xmlDerSettings = derService.getDerSettingsHttp(endDeviceId, derId);
               
               Map<String, String> derReactivePowerTests = Map.of(
                       "derCapability", xmlDerCapability,
                       "derSettings", xmlDerSettings
                   );
                Map<String, Object> derReactivePowerTestResults = derUtil.reactivePowerHandler(derReactivePowerTests);
                
                // Build combined XML for validation
                @SuppressWarnings("unchecked")
                Map<String, String> modeValidation = (Map<String, String>) derReactivePowerTestResults.get("modeValidation");
                @SuppressWarnings("unchecked")
                Map<String, String> validationChecks = (Map<String, String>) derReactivePowerTestResults.get("validationChecks");
                String combinedXml = derUtil.buildReactivePowerXml(xmlDerCapability, xmlDerSettings, modeValidation, validationChecks);
                
                LOGGER.info("Combined XML for reactivepower validation: " + combinedXml);
                
                // Trigger XML validation (display only, no expected XML comparison)
                if (xmlValidationService != null) {
                    LOGGER.info("Triggering XML validation for reactivepower");
                    xmlValidationService.displayXmlOnly(
                        "/edev/" + endDeviceId + "/der/" + derId + "/reactivepower",
                        "GET",
                        combinedXml
                    );
                } else {
                    LOGGER.warning("xmlValidationService is null, skipping validation");
                }
                
                return derReactivePowerTestResults;
           }
           return null;
    }









}
