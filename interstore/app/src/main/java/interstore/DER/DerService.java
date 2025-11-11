package interstore.DER;
import interstore.EndDevice.EndDeviceDto;
import interstore.EndDevice.EndDeviceRepository;
import interstore.Identity.SubscribableResourceEntity;
import interstore.Identity.SubscribableResourceRepository;
import interstore.Types.*;
import interstore.Types.DERType; 
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.List;

@Service
public class DerService {
    @Autowired
    private DerRepository derRepository;
    @Autowired
    private EndDeviceRepository endDeviceRepository;
    @Autowired
    SubscribableResourceRepository subscribableResourceRepository;
    private static final Logger LOGGER = Logger.getLogger(DerService.class.getName());

    @Transactional
    public DerEntity createDerCapability(JSONObject payload)throws NumberFormatException, JSONException, NotFoundException  {
        LOGGER.info("Received DER payload is " + payload); 
        DerEntity derEntity = new DerEntity();
        Long endDeviceId = Long.parseLong(payload.getJSONObject("payload").getString("endDeviceId")); 
        EndDeviceDto endDevice = endDeviceRepository.findById( endDeviceId)
        .orElseThrow(() -> new NotFoundException());
        SubscribableResourceEntity subscribableResourceEntity = new SubscribableResourceEntity();
        String derListLink = endDevice.getDERListLink();
        LOGGER.info("the DER listlink is " + derListLink);
        derEntity.setEndDevice(endDevice); 
        derEntity.setSubscribableResourceList(subscribableResourceEntity);
        try {
            derEntity  = derRepository.save(derEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER entity", e);
        }
        try {
            subscribableResourceRepository.save(subscribableResourceEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving subscribableResourceEntity entity", e);
        }
        setDerCapability(derEntity  , payload,  derListLink);
        derEntity = derRepository.save(derEntity);
        return  derEntity;
       
    }
    /*The DER Availability is a unique profile that each end device has when it's manufactured 
     * this can't be changed because these are the rated values of an End Device , In the test 
     * application while create a DER resource it needs these rated values to be set which is
     * called  DER capablity . every time when the uri hit with /dercap this will bring the 
     * capability of that end device . Der Settigns on the other hand is the values that can be
     * changed by the user . The issue is while creating the DER resource other attributes of 
     * DER resource intialized as null but later the values intailised as null has to editable  , 
     *  
     */
    public void setDerCapability(DerEntity derEntity, JSONObject payload, String derListLink )
    {
        Long Derid = derEntity.getId();
        String DeridString = "/"+ String.valueOf(Derid);
        String backslashUri = "/";
        JSONObject Derpayload = payload.optJSONObject("payload");
        String derLink = derListLink + DeridString ; 
        String derCapabilityLink = derListLink + DeridString + backslashUri + Derpayload.optString("derCapabilityLink", null);
        String derStatusLink = derListLink + DeridString + backslashUri + Derpayload.optString("derStatusLink", null);
        String derAvailabilityLink =  derListLink + DeridString + backslashUri + Derpayload.optString("derAvailabilityLink", null);
        String derSettingsLink =  derListLink + DeridString + backslashUri + Derpayload.optString("derSettingsLink", null);
        String associatedUsagePointLink = derListLink + DeridString + backslashUri + Derpayload.optString("associatedUsagePointLink", null);
        String associatedDERProgramListLink = derListLink + DeridString + backslashUri + Derpayload.optString("associatedDERProgramListLink", null);
        String currentDERProgramLink = derListLink + DeridString + backslashUri + Derpayload.optString("currentDERProgramLink", null); 
        derEntity.setDerLink(derLink); 
        derEntity.setDerCapabilityLink(derCapabilityLink);
        derEntity.setDerStatusLink(derStatusLink);
        derEntity.setDerAvailabilityLink(derAvailabilityLink);
        derEntity.setDerSettingsLink(derSettingsLink); 
        derEntity.setAssociatedUsagePointLink(associatedUsagePointLink);
        derEntity.setAssociatedDERProgramListLink(associatedDERProgramListLink);
        derEntity.setCurrentDERProgramLink(currentDERProgramLink);
        // DER Capabilities 
        // Der control type has to be avialable from the Der controls which is allready created 
        // the payload will have the informationof the type of the DER control , it has to pick the 
        // corresponding object with the der control type from the data base . 
        String modesSupported = Derpayload.optString("modesSupported");
        Integer rtgAbnormalCategoryUINT8 = Derpayload.optInt("rtgAbnormalCategory", 0);
        Double rtgMaxADouble = Derpayload.optDouble("rtgMaxA", Double.NaN);
        Double rtgMaxAhDouble = Derpayload.optDouble("rtgMaxAh", Double.NaN);
        Double rtgMaxChargeRateDouble = Derpayload.optDouble("rtgMaxChargeRateVA", Double.NaN);
        Double rtgMaxChargeRateWDouble = Derpayload.optDouble("rtgMaxChargeRateW", Double.NaN);
        Double rtgMaxDischargeRateVADouble = Derpayload.optDouble("rtgMaxDischargeRateVA", Double.NaN);
        Double rtgMaxDischargeRateWDouble = Derpayload.optDouble("rtgMaxDischargeRateW", Double.NaN);
        Double rtgMaxVDouble = Derpayload.optDouble("rtgMaxV", Double.NaN);
        Double rtgMaxVADouble = Derpayload.optDouble("rtgMaxVA", Double.NaN);
        Double rtgMaxVarDouble = Derpayload.optDouble("rtgMaxVar", Double.NaN);
        Double rtgMaxVarNegDouble = Derpayload.optDouble("rtgMaxVarNeg", Double.NaN);
        Double rtgMaxWDouble = Derpayload.optDouble("rtgMaxW", Double.NaN);
        Double rtgMaxWhDouble = Derpayload.optDouble("rtgMaxWh", Double.NaN);
        Double rtgMinPFOverExcitedDouble = Derpayload.optDouble("rtgMinPFOverExcited", Double.NaN);
        Double rtgMinPFUnderExcitedDouble = Derpayload.optDouble("rtgMinPFUnderExcited", Double.NaN);
        Double rtgMinVDouble = Derpayload.optDouble("rtgMinV", Double.NaN);
        Integer rtgNormalCategoryUINT8 = Derpayload.optInt("rtgNormalCategory", 0);
        Double rtgOverExcitedPFDouble = Derpayload.optDouble("rtgOverExcitedPF", Double.NaN);
        Double rtgOverExcitedWDouble = Derpayload.optDouble("rtgOverExcitedW", Double.NaN);
        Double rtgReactiveSusceptanceDouble = Derpayload.optDouble("rtgReactiveSusceptance", Double.NaN);
        Double rtgUnderExcitedPFDouble = Derpayload.optDouble("rtgUnderExcitedPF", Double.NaN);
        Double rtgUnderExcitedWDouble = Derpayload.optDouble("rtgUnderExcitedW", Double.NaN);
        Double rtgVNomDouble = Derpayload.optDouble("rtgVNom", Double.NaN);
        Integer derType = Derpayload.optInt("derType", 0);
        // calling setters from the entity class 
        derEntity.setModesSupported(modesSupported);
        derEntity.setRtgAbnormalCategory(rtgAbnormalCategoryUINT8);
        derEntity.setRtgMaxA(rtgMaxADouble);
        derEntity.setRtgMaxAh(rtgMaxAhDouble);
        derEntity.setRtgMaxChargeRateVA(rtgMaxChargeRateDouble);
        derEntity.setRtgMaxChargeRateW(rtgMaxChargeRateWDouble);
        derEntity.setRtgMaxDischargeRateVA(rtgMaxDischargeRateVADouble);
        derEntity.setRtgMaxDischargeRateW(rtgMaxDischargeRateWDouble);
        derEntity.setRtgMaxV(rtgMaxVDouble);
        derEntity.setRtgMaxVA(rtgMaxVADouble);
        derEntity.setRtgMaxVar(rtgMaxVarDouble);
        derEntity.setRtgMaxVarNeg(rtgMaxVarNegDouble);
        derEntity.setRtgMaxW(rtgMaxWDouble);
        derEntity.setRtgMaxWh(rtgMaxWhDouble);
        derEntity.setRtgMinPFOverExcited(rtgMinPFOverExcitedDouble);
        derEntity.setRtgMinPFUnderExcited(rtgMinPFUnderExcitedDouble);
        derEntity.setRtgMinV(rtgMinVDouble);
        derEntity.setRtgNormalCategory(rtgNormalCategoryUINT8);
        derEntity.setRtgOverExcitedPF(rtgOverExcitedPFDouble);
        derEntity.setRtgOverExcitedW(rtgOverExcitedWDouble);
        derEntity.setRtgReactiveSusceptance(rtgReactiveSusceptanceDouble);
        derEntity.setRtgUnderExcitedPF(rtgUnderExcitedPFDouble);
        derEntity.setRtgUnderExcitedW(rtgUnderExcitedWDouble);
        derEntity.setRtgVNom(rtgVNomDouble);
        derEntity.setDerType(derType); 
        

    }
 
    @Transactional
    public  DerEntity createDerSettings(JSONObject payload)throws NumberFormatException, JSONException, NotFoundException {
        LOGGER.info("Received DER payload is in Der Settings " + payload); 
        Long endDeviceId = Long.parseLong(payload.getJSONObject("payload").getString("endDeviceId")); 
        EndDeviceDto endDevice = endDeviceRepository.findById( endDeviceId)
        .orElseThrow(() -> new NotFoundException());
        LOGGER.info("Received DER payload is " + payload); 
        DerEntity derEntity = new DerEntity();
        SubscribableResourceEntity subscribableResourceEntity = new SubscribableResourceEntity();
        String derListLink = endDevice.getDERListLink();
        LOGGER.info("the DER listlink is " + derListLink);
        derEntity.setEndDevice(endDevice);
        derEntity.setSubscribableResourceList(subscribableResourceEntity);
        try {
            derEntity  = derRepository.save(derEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER entity", e);
        }
        try {
            subscribableResourceRepository.save(subscribableResourceEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving subscribableResourceEntity entity", e);
        }
        setDerSettings(derEntity  , payload, derListLink);
        derEntity = derRepository.save(derEntity);
        return  derEntity;

    }
    

    /*
     * {"derSettings":"derSettings","payload":{"setESRampTms":"10","setMinPFUnderExcited":"0.95","setESHighVolt":"264","setESHighFreq":"60.5",
     * "setMaxVarNeg":"-400","setMaxWh":"2640","setGradW":"10","setMinV":"208","setMaxDischargeRateW":"480","setSoftGradW":"2","setMaxA":"250","setMaxVA":"500",
     * "setMaxChargeRateVA":"500","setMaxChargeRateW":"480","derID":"1","modesEnabled":"6","setESRandomDelay":"3","setMaxDischargeRateVA":"500","setVNom":"240",
     * "setMinPFOverExcited":"0.95","setVRef":"240","setESDelay":"5",
     * "endDeviceId":"1","setMaxW":"4800","setMaxV":"264","setESLowVolt":"208","setMaxVar":"400"},"action":"post","servicename":"createDerSettingsmanager"}
     */
    public void setDerSettings(DerEntity derEntity, JSONObject payload, String derListLink) {

        LOGGER.info("Received payload inside Set DER Settings : " +  payload.toString());
        Long Derid = derEntity.getId();
        String DeridString = "/"+ String.valueOf(Derid);
        JSONObject derSettingsPayload = payload.optJSONObject("payload");
        String derLink = derListLink + DeridString ;
        String backslashUri = "/";
        String derSettingsLink =  derListLink + DeridString + backslashUri + derSettingsPayload.optString("derSettingsLink", null);
        derEntity.setDerLink(derLink); 
        derEntity.setDerSettingsLink(derSettingsLink); 
    
        Integer modesEnabled = derSettingsPayload.optInt("modesEnabled", 0);
        Long setESDelay = parseLongFromPayload(derSettingsPayload, "setESDelay");
        Long setESHighFreq = parseLongFromPayload(derSettingsPayload, "setESHighFreq");
        Long setESLowFreq = parseLongFromPayload(derSettingsPayload, "setESLowFreq");
        Long setESHighVolt = parseLongFromPayload(derSettingsPayload, "setESHighVolt");
        Long setESLowVolt = parseLongFromPayload(derSettingsPayload, "setESLowVolt");
        Long setESRampTms = parseLongFromPayload(derSettingsPayload, "setESRampTms");
        Long setESRandomDelay = parseLongFromPayload(derSettingsPayload, "setESRandomDelay");
        Long setGradW = parseLongFromPayload(derSettingsPayload, "setGradW");
        Long setSoftGradW = parseLongFromPayload(derSettingsPayload, "setSoftGradW");
        Double setMaxA = parseDoubleFromPayload(derSettingsPayload, "setMaxA");
        Double setMaxChargeRateVA = parseDoubleFromPayload(derSettingsPayload, "setMaxChargeRateVA");
        Double setMaxChargeRateW = parseDoubleFromPayload(derSettingsPayload, "setMaxChargeRateW");
        Double setMaxDischargeRateVA = parseDoubleFromPayload(derSettingsPayload, "setMaxDischargeRateVA");
        Double setMaxDischargeRateW = parseDoubleFromPayload(derSettingsPayload, "setMaxDischargeRateW");
        Double setMaxV = parseDoubleFromPayload(derSettingsPayload, "setMaxV");
        Double setMaxVA = parseDoubleFromPayload(derSettingsPayload, "setMaxVA");
        Double setMaxVar = parseDoubleFromPayload(derSettingsPayload, "setMaxVar");
        Double setMaxVarNeg = parseDoubleFromPayload(derSettingsPayload, "setMaxVarNeg");
        Double setMaxW = parseDoubleFromPayload(derSettingsPayload, "setMaxW");
        Double setMaxWh = parseDoubleFromPayload(derSettingsPayload, "setMaxWh");
        Double setMinPFOverExcited = parseDoubleFromPayload(derSettingsPayload, "setMinPFOverExcited");
        Double setMinPFUnderExcited = parseDoubleFromPayload(derSettingsPayload, "setMinPFUnderExcited");
        Double setMinV = parseDoubleFromPayload(derSettingsPayload, "setMinV");
        Double setVNom = parseDoubleFromPayload(derSettingsPayload, "setVNom");
        Double setVRef = parseDoubleFromPayload(derSettingsPayload, "setVRef");
      
        // Calling setters from the entity class
        derEntity.setModesEnabled(modesEnabled);
        derEntity.setSetESDelay(setESDelay);
        derEntity.setSetESHighFreq(setESHighFreq);
        derEntity.setSetESLowFreq(setESLowFreq);
        derEntity.setSetESHighVolt(setESHighVolt);
        derEntity.setSetESLowVolt(setESLowVolt);
        derEntity.setSetESRampTms(setESRampTms);
        derEntity.setSetESRandomDelay(setESRandomDelay);
        derEntity.setSetGradW(setGradW);
        derEntity.setSetSoftGradW(setSoftGradW);
        derEntity.setSetMaxA(setMaxA);
        derEntity.setSetMaxChargeRateVA(setMaxChargeRateVA);
        derEntity.setSetMaxChargeRateW(setMaxChargeRateW);
        derEntity.setSetMaxDischargeRateVA(setMaxDischargeRateVA);
        derEntity.setSetMaxDischargeRateW(setMaxDischargeRateW);
        derEntity.setSetMaxV(setMaxV);
        derEntity.setSetMaxVA(setMaxVA);
        derEntity.setSetMaxVar(setMaxVar);
        derEntity.setSetMaxVarNeg(setMaxVarNeg);
        derEntity.setSetMaxW(setMaxW);
        derEntity.setSetMaxWh(setMaxWh);
        derEntity.setSetMinPFOverExcited(setMinPFOverExcited);
        derEntity.setSetMinPFUnderExcited(setMinPFUnderExcited);
        derEntity.setSetMinV(setMinV);
        derEntity.setSetVNom(setVNom);
        derEntity.setSetVRef(setVRef);
    }

    private Long parseLongFromPayload(JSONObject payload, String key) {
        try {
            return payload.has(key) ? payload.optLong(key, 0) : 0;
        } catch (Exception e) {
            return 0L;
        }
    }
    
  
    private Double parseDoubleFromPayload(JSONObject payload, String key) {
        try {
            return payload.has(key) ? payload.optDouble(key, Double.NaN) : Double.NaN;
        } catch (Exception e) {
            return Double.NaN;
        }
    }
    

    public ResponseEntity<Map<String, Object>> getDerCapability( Long EndDeviceId, Long derID) {
        try {
            Map<String, Object> result = new HashMap<>();
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId( EndDeviceId , derID);
    
            if (derEntityOptional.isPresent()) {
                DerEntity derEntity = derEntityOptional.get();
                Map<String, Object> entityMap = new HashMap<>();
                entityMap.put("id", derEntity.getId());
                entityMap.put("modesSupported", derEntity.getModesSupported());
                entityMap.put("rtgAbnormalCategory", derEntity.getRtgAbnormalCategory());
                entityMap.put("rtgMaxA", derEntity.getRtgMaxA());
                entityMap.put("rtgMaxAh", derEntity.getRtgMaxAh());
                entityMap.put("rtgMaxChargeRateVA", derEntity.getRtgMaxChargeRateVA());
                entityMap.put("rtgMaxChargeRateW", derEntity.getRtgMaxChargeRateW());
                entityMap.put("rtgMaxDischargeRateVA", derEntity.getRtgMaxDischargeRateVA());
                entityMap.put("rtgMaxDischargeRateW", derEntity.getRtgMaxDischargeRateW());
                entityMap.put("rtgMaxV", derEntity.getRtgMaxV());
                entityMap.put("rtgMaxVA", derEntity.getRtgMaxVA());
                entityMap.put("rtgMaxVar", derEntity.getRtgMaxVar());
                entityMap.put("rtgMaxVarNeg", derEntity.getRtgMaxVarNeg());
                entityMap.put("rtgMaxW", derEntity.getRtgMaxW());
                entityMap.put("rtgMaxWh", derEntity.getRtgMaxWh());
                entityMap.put("rtgMinPFOverExcited", derEntity.getRtgMinPFOverExcited());
                entityMap.put("rtgMinPFUnderExcited", derEntity.getRtgMinPFUnderExcited());
                entityMap.put("rtgMinV", derEntity.getRtgMinV());
                entityMap.put("rtgNormalCategory", derEntity.getRtgNormalCategory());
                entityMap.put("rtgOverExcitedPF", derEntity.getRtgOverExcitedPF());
                entityMap.put("rtgOverExcitedW", derEntity.getRtgOverExcitedW());
                entityMap.put("rtgReactiveSusceptance", derEntity.getRtgReactiveSusceptance());
                entityMap.put("rtgUnderExcitedPF", derEntity.getRtgUnderExcitedPF());
                entityMap.put("rtgUnderExcitedW", derEntity.getRtgUnderExcitedW());
                entityMap.put("rtgVNom", derEntity.getRtgVNom());
                entityMap.put("derLink", derEntity.getDerLink()); 
                entityMap.put("derCapabilityLink", derEntity.getDerCapabilityLink());
                Integer derTypeInt = derEntity.getDerType();
                short derTypeshort = derTypeInt.shortValue();
                DERType derType = new DERType(derTypeshort); 
                String derTypeString = derType.getDescription();
                entityMap.put("derType", derTypeString);
                result.put("DerCapability", entityMap);  
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "DER entity not found"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DER entity", e);
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }
    
    public ResponseEntity<Map<String, Object>> getDerSettings(Long EndDeviceId, Long derID)

    {
        try {
            Map<String, Object> result = new HashMap<>();
            
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(EndDeviceId , derID);
    
            if (derEntityOptional.isPresent())
            {
                DerEntity derEntity = derEntityOptional.get();
                Map<String, Object> entityMap = new HashMap<>();
                entityMap.put("modesEnabled", derEntity.getModesEnabled());
                entityMap.put("setESDelay", derEntity.getSetESDelay());
                entityMap.put("setESHighFreq", derEntity.getSetESHighFreq());
                entityMap.put("setESLowFreq", derEntity.getSetESLowFreq());
                entityMap.put("setESHighVolt", derEntity.getSetESHighVolt());
                entityMap.put("setESLowVolt", derEntity.getSetESLowVolt());
                entityMap.put("setESRampTms", derEntity.getSetESRampTms());
                entityMap.put("setESRandomDelay", derEntity.getSetESRandomDelay());
                entityMap.put("setGradW", derEntity.getSetGradW());
                entityMap.put("setSoftGradW", derEntity.getSetSoftGradW());
                entityMap.put("setMaxA", derEntity.getSetMaxA());
                entityMap.put("setMaxChargeRateVA", derEntity.getSetMaxChargeRateVA());
                entityMap.put("setMaxChargeRateW", derEntity.getSetMaxChargeRateW());
                entityMap.put("setMaxDischargeRateVA", derEntity.getSetMaxDischargeRateVA());
                entityMap.put("setMaxDischargeRateW", derEntity.getSetMaxDischargeRateW());
                
                entityMap.put("setMaxV", derEntity.getSetMaxV());
                entityMap.put("setMaxVA", derEntity.getSetMaxVA());
                entityMap.put("setMaxVar", derEntity.getSetMaxVar());
                entityMap.put("setMaxVarNeg", derEntity.getSetMaxVarNeg());
                entityMap.put("setMaxW", derEntity.getSetMaxW());
                entityMap.put("setMaxWh", derEntity.getSetMaxWh());
                entityMap.put("setMinPFOverExcited", derEntity.getSetMinPFOverExcited());
                entityMap.put("setMinPFUnderExcited", derEntity.getSetMinPFUnderExcited());
                entityMap.put("setMinV", derEntity.getSetMinV());
                entityMap.put("setVNom", derEntity.getSetVNom());
                entityMap.put("setVRef", derEntity.getSetVRef());
                result.put("DerSettings", entityMap);
                return ResponseEntity.ok(result);
               }
                else {
                    return ResponseEntity.status(404).body(Map.of("error", "DER entity not found"));
                }
            }  catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error retrieving DER entity", e);
                return ResponseEntity.status(500).body(Map.of("error", "Server error"));
            }     
    }

    public ResponseEntity<Map<String, Object>> getDer(Long derId, Long endDeviceId){
        Map<String, Object> result = new HashMap<>();
            
        Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId , derId);
        if (derEntityOptional.isPresent())
        {
            DerEntity derEntity = derEntityOptional.get();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", derEntity.getId());
            entityMap.put("derLink", derEntity.getDerLink()); 
            entityMap.put("derCapabilityLink", derEntity.getDerCapabilityLink());
            entityMap.put("derStatusLink", derEntity.getDerStatusLink());
            entityMap.put("derAvailabilityLink", derEntity.getDerAvailabilityLink());
            entityMap.put("derSettingsLink", derEntity.getDerSettingsLink()); 
            entityMap.put("associatedUsagePointLink", derEntity.getAssociatedUsagePointLink());
            entityMap.put("associatedDERProgramListLink", derEntity.getAssociatedDERProgramListLink());
            entityMap.put("currentDERProgramLink", derEntity.getCurrentDERProgramLink()); 
            result.put("Der", entityMap);  
            return ResponseEntity.ok(result);
        }
        return null;
    }

    public String getDerHttp(Long endDeviceId, Long derId) {
        try {
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId, derId);

            if (derEntityOptional.isEmpty()) {
                return "<DER xmlns=\"http://ieee.org/2030.5\" href=\"/edev/" + endDeviceId + "/der/" + derId + "\">\n" +
                    " <message>No DER found for EndDevice " + endDeviceId + " and DER ID " + derId + "</message>\n" +
                    "</DER>";
            }

            DerEntity derEntity = derEntityOptional.get();
            StringBuilder xml = new StringBuilder();
            xml.append("<DER xmlns=\"http://ieee.org/2030.5\" ")
            .append("href=\"").append(stripHost(derEntity.getDerLink())).append("\">\n");
            appendIfPresent(xml, "DERCapabilityLink", derEntity.getDerCapabilityLink());
            appendIfPresent(xml, "DERStatusLink", derEntity.getDerStatusLink());
            appendIfPresent(xml, "DERAvailabilityLink", derEntity.getDerAvailabilityLink());
            appendIfPresent(xml, "DERSettingsLink", derEntity.getDerSettingsLink());
            appendIfPresent(xml, "AssociatedUsagePointLink", derEntity.getAssociatedUsagePointLink());
            appendIfPresent(xml, "AssociatedDERProgramListLink", derEntity.getAssociatedDERProgramListLink());
            appendIfPresent(xml, "CurrentDERProgramLink", derEntity.getCurrentDERProgramLink());

            xml.append("</DER>");
            return xml.toString();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DER", e);
            return "<DER xmlns=\"http://ieee.org/2030.5\" href=\"/edev/" + endDeviceId + "/der/" + derId + "\">\n" +
                " <error>Some error occurred</error>\n" +
                "</DER>";
        }
    }

    private void appendIfPresent(StringBuilder xml, String tagName, String link) {
        if (link == null || link.isBlank()) return;

        String href = stripHost(link);
        if (href == null || href.isBlank()) return;

        xml.append(" <").append(tagName)
        .append(" href=\"").append(href).append("\"");
        if (tagName.endsWith("ListLink")) {
            xml.append(" all=\"0\"");
        }
        xml.append("/>\n");
    }

    

    private String stripHost(String url) {
        if (url == null) return null;
        try {
            java.net.URI uri = new java.net.URI(url);
            String path = uri.getPath(); // "/derp" or "/2030.5/dcap/tm"
            if (path == null || path.isEmpty()) return "/";

            // If you want to specifically remove "/2030.5" prefix
            String prefix = "/2030.5";
            if (path.startsWith(prefix)) {
                path = path.substring(prefix.length());
                if (path.isEmpty()) path = "/"; // ensure at least "/"
            }
            return path;
        } catch (Exception e) {
            // fallback: naive parsing
            int idx = url.indexOf("://");
            if (idx != -1) {
                String remainder = url.substring(idx + 3); // skip "http://"
                int slashIdx = remainder.indexOf("/");
                if (slashIdx != -1) {
                    return remainder.substring(slashIdx); // return path after host
                } else {
                    return "/"; // no path, return root
                }
            }
            return url; // unknown format
        }
    }

    public String getAllDERsHttp(Long endDeviceId) {
    try {
        List<DerEntity> derEntityList = derRepository.findByEndDeviceId(endDeviceId);
        Optional<EndDeviceDto> endDeviceDto = endDeviceRepository.findById(endDeviceId);
        if (endDeviceDto.isEmpty()) {
            return "<DERList xmlns=\"XXXXXXXXXXXXXXXXXXXXXX\" href=\"/edev/" + endDeviceId + "/der\" all=\"0\" results=\"0\">\n" +
                " <message>No EndDevice found for ID " + endDeviceId + "</message>\n" +
                "</DERList>";
        }
        String derListLink = stripHost(endDeviceDto.get().getDERListLink());

        StringBuilder xml = new StringBuilder();
        xml.append("<DERList xmlns=\"http://ieee.org/2030.5\" ")
           .append("href=\"").append(derListLink).append("\" ")
           .append("all=\"").append(derEntityList.size()).append("\" ")
           .append("results=\"").append(derEntityList.size()).append("\">\n");

        if (derEntityList.isEmpty()) {
            xml.append(" <message>No DERs found for EndDevice ").append(endDeviceId).append("</message>\n");
        } else {
            for (DerEntity der : derEntityList) {
                xml.append(" <DER href=\"").append(stripHost(der.getDerLink())).append("\">\n");

                // Conditional link elements
                appendIfPresentIndented(xml, "DERCapabilityLink", der.getDerCapabilityLink(), 2);
                appendIfPresentIndented(xml, "DERStatusLink", der.getDerStatusLink(), 2);
                appendIfPresentIndented(xml, "DERAvailabilityLink", der.getDerAvailabilityLink(), 2);
                appendIfPresentIndented(xml, "DERSettingsLink", der.getDerSettingsLink(), 2);
                appendIfPresentIndented(xml, "AssociatedUsagePointLink", der.getAssociatedUsagePointLink(), 2);
                appendIfPresentIndented(xml, "AssociatedDERProgramListLink", der.getAssociatedDERProgramListLink(), 2);
                appendIfPresentIndented(xml, "CurrentDERProgramLink", der.getCurrentDERProgramLink(), 2);

                xml.append(" </DER>\n");
            }
        }

        xml.append("</DERList>");
        return xml.toString();

    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error retrieving DERs", e);
        return "<DERList href=\"/edev/" + endDeviceId + "/der\" all=\"0\" results=\"0\">\n" +
               " <error>Some error occurred</error>\n" +
               "</DERList>";
        }
    }

    /**
     * Helper to append XML tags only when the link is present.
     * Adds indentation for readability.
     */
    private void appendIfPresentIndented(StringBuilder xml, String tagName, String link, int indentLevel) {
        if (link != null && !link.isBlank()) {
            String indent = " ".repeat(indentLevel);
            xml.append(indent)
            .append("<").append(tagName)
            .append(" href=\"").append(stripHost(link)).append("\"");
            if (tagName.endsWith("ListLink")) {
                xml.append(" all=\"0\"");
            }
            xml.append("/>\n");
        }
    }

    @Transactional
     public ResponseEntity<Map<String, Object>> updatePowerGenerationTest(Long endDeviceID, Long derId,JSONObject payload)throws NumberFormatException, JSONException, NotFoundException 
     {
        LOGGER.info("Received DER power generation payload is " + payload); 
        Long endDeviceId = Long.parseLong(payload.getString("endDeviceId")); 
        EndDeviceDto endDevice = endDeviceRepository.findById( endDeviceId)
        .orElseThrow(() -> new NotFoundException());
        Long derID = Long.parseLong(payload.getString("derID")); 
        LOGGER.info("DER service Power generation test is " + endDeviceId  + " and " + derID );
        DerEntity derEntity = derRepository.findById(derID)
        .orElseThrow(() -> new NotFoundException());
        derEntity.setEndDevice(endDevice); 
        try {
            derEntity  = derRepository.save(derEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER entity", e);
        }

        setPowerGeneration(derEntity  , payload);
        derEntity = derRepository.save(derEntity);
        try {
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId, derID);
            Map<String, Object> result = new HashMap<>();
            derEntity = derEntityOptional.get();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", derEntity.getId());
            entityMap.put("setMaxW", derEntity.getSetMaxW());
            entityMap.put("setMaxVA", derEntity.getSetMaxVA());
            LOGGER.info("DER service Power generation test is " + derEntity.getSetMaxW() + " and " + derEntity.getSetMaxVA());
            result.put("DerPowerGenerationTest", entityMap);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DER entity", e);
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
    
     }
    }

     public void setPowerGeneration(DerEntity derEntity, JSONObject derPowerGenerationpayload)
     {  
        //JSONObject DerPowerGenerationpayload = payload.optJSONObject("payload");
        Double setMaxW = parseDoubleFromPayload(  derPowerGenerationpayload, "setMaxW");
        Double setMaxVA = parseDoubleFromPayload(  derPowerGenerationpayload, "setMaxVA");
        derEntity.setSetMaxW(setMaxW);
        derEntity.setSetMaxVA(setMaxVA);
     }

    
     //public void reactivePoweropModFixedVAr( Long EndDeviceId, Long derID)
     /*check that opModFixedVAr is present or not 
     multiple modes can be supported in the der capability 
          {
        "DERCapability": {
        "modesSupported": ["opModFixedVAr", "opModVoltVar"],
        "rtgMaxVar": 100,
        "rtgMaxVarNeg": -100,
        "rtgVNom": 240,
        "type": "Battery"
        }
      }
        {  
        try {
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId( EndDeviceId , derID);
            DerEntity derEntity = derEntityOptional.get();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", derEntity.getId());
            entityMap.put("modesSupported", derEntity.getModesSupported());
            entityMap.put("rtgMaxVar", derEntity.getRtgMaxVar());
        //}   
       
    
       // LOGGER.info("Received payload inside Set DER Settings : " +  payload.toString());
        //JSONObject derSettingsPayload = payload.optJSONObject("payload");
       // Integer modesEnabled = derSettingsPayload.optInt("modesEnabled", 0);
       // Double setMaxW = parseDoubleFromPayload(derPowerGenerationpayload, "setMaxW");
        //Double setMaxVA = parseDoubleFromPayload(derPowerGenerationpayload, "setMaxVA");
        //derEntity.setSetMaxW(setMaxW);
        //derEntity.setSetMaxVA(setMaxVA);
     }
      *  
      */
     
}



