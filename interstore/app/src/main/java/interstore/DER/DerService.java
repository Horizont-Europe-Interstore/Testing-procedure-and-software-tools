package interstore.DER;
import interstore.DER.DERCapability.DERCapabilityEntity;
import interstore.DER.DERCapability.DERCapabilityRepository;
import interstore.DER.DERSettings.DERSettingsEntity;
import interstore.DER.DERSettings.DERSettingsRepository;
import interstore.EndDevice.EndDeviceEntity;
import interstore.EndDevice.EndDeviceRepository;
import interstore.Identity.SubscribableResourceEntity;
import interstore.Identity.SubscribableResourceRepository;
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

    @Autowired
    private DERCapabilityRepository derCapabilityRepository;

    @Autowired
    private DERSettingsRepository derSettingsRepository;

    @Transactional
    public DerEntity createDerEntity(JSONObject payload)throws NumberFormatException, JSONException, NotFoundException  {
        LOGGER.info("Received DER payload is " + payload); 
        DerEntity derEntity = new DerEntity();
        Long endDeviceId = Long.parseLong(payload.getJSONObject("payload").getString("endDeviceID")); 
        EndDeviceEntity endDevice = endDeviceRepository.findById( endDeviceId)
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
        setDerEntity(derEntity  , payload,  derListLink);
        derEntity = derRepository.save(derEntity);
        return  derEntity;
       
    }

     public void setDerEntity(DerEntity derEntity, JSONObject payload, String derListLink )
    {
        Long Derid = derEntity.getId();
        String DeridString = "/"+ String.valueOf(Derid);
        String backslashUri = "/";
        JSONObject Derpayload = payload.optJSONObject("payload");
        String derLink = derListLink + DeridString ; 
        String capLink = Derpayload.optString("derCapabilityLink", null);
        if (capLink != null && !capLink.trim().isEmpty()) {
            derEntity.setDerCapabilityLink(derListLink + DeridString + backslashUri + capLink.trim());
        }

        String statusLink = Derpayload.optString("derStatusLink", null);
        if (statusLink != null && !statusLink.trim().isEmpty()) {
            derEntity.setDerStatusLink(derListLink + DeridString + backslashUri + statusLink.trim());
        }

        String availLink = Derpayload.optString("derAvailabilityLink", null);
        if (availLink != null && !availLink.trim().isEmpty()) {
            derEntity.setDerAvailabilityLink(derListLink + DeridString + backslashUri + availLink.trim());
        }

        String settingsLink = Derpayload.optString("derSettingsLink", null);
        if (settingsLink != null && !settingsLink.trim().isEmpty()) {
            derEntity.setDerSettingsLink(derListLink + DeridString + backslashUri + settingsLink.trim());
        }

        String usagePointLink = Derpayload.optString("associatedUsagePointLink", null);
        if (usagePointLink != null && !usagePointLink.trim().isEmpty()) {
            derEntity.setAssociatedUsagePointLink(derListLink + DeridString + backslashUri + usagePointLink.trim());
        }

        String derProgramListLink = Derpayload.optString("associatedDERProgramListLink", null);
        if (derProgramListLink != null && !derProgramListLink.trim().isEmpty()) {
            derEntity.setAssociatedDERProgramListLink(derListLink + DeridString + backslashUri + derProgramListLink.trim());
        }

        String currentProgramLink = Derpayload.optString("currentDERProgramLink", null);
        if (currentProgramLink != null && !currentProgramLink.trim().isEmpty()) {
            derEntity.setCurrentDERProgramLink(derListLink + DeridString + backslashUri + currentProgramLink.trim());
        }
        derEntity.setDerLink(derLink);    
    }
    
    @Transactional
    public DERCapabilityEntity createDerCapability(JSONObject payload)throws NumberFormatException, JSONException, NotFoundException  {
        LOGGER.info("Received DER payload is " + payload);
        Long derId = Long.parseLong(payload.getJSONObject("payload").getString("derId"));
        DerEntity derEntity = derRepository.findById( derId)
        .orElseThrow(() -> new NotFoundException());
        DERCapabilityEntity derCapabilityEntity = new DERCapabilityEntity();
        derCapabilityEntity.setDerEntity(derEntity);
        if(derEntity.getDerCapabilityLink().equals(null)){
        String backslashUri = "/";
        String derCapabilityLink = derEntity.getDerLink() + backslashUri + payload.optJSONObject("payload").optString("derCapabilityLink", null);
        derCapabilityEntity.setDerCapabilityLink(derCapabilityLink);
        } else{
            derCapabilityEntity.setDerCapabilityLink(derEntity.getDerCapabilityLink());
        }
        try {
            derCapabilityEntity  = derCapabilityRepository.save(derCapabilityEntity);
            derEntity.setDerCapability(derCapabilityEntity); 
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER Capability entity", e);
        }
        setDerCapability(derCapabilityEntity  , payload);
        derCapabilityEntity = derCapabilityRepository.save(derCapabilityEntity);
        return  derCapabilityEntity;
       
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
    public void setDerCapability(DERCapabilityEntity derCapabilityEntity, JSONObject payload )
    {
        JSONObject Derpayload = payload.optJSONObject("payload");
        
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
        derCapabilityEntity.setModesSupported(modesSupported);
        derCapabilityEntity.setRtgAbnormalCategory(rtgAbnormalCategoryUINT8);
        derCapabilityEntity.setRtgMaxA(rtgMaxADouble);
        derCapabilityEntity.setRtgMaxAh(rtgMaxAhDouble);
        derCapabilityEntity.setRtgMaxChargeRateVA(rtgMaxChargeRateDouble);
        derCapabilityEntity.setRtgMaxChargeRateW(rtgMaxChargeRateWDouble);
        derCapabilityEntity.setRtgMaxDischargeRateVA(rtgMaxDischargeRateVADouble);
        derCapabilityEntity.setRtgMaxDischargeRateW(rtgMaxDischargeRateWDouble);
        derCapabilityEntity.setRtgMaxV(rtgMaxVDouble);
        derCapabilityEntity.setRtgMaxVA(rtgMaxVADouble);
        derCapabilityEntity.setRtgMaxVar(rtgMaxVarDouble);
        derCapabilityEntity.setRtgMaxVarNeg(rtgMaxVarNegDouble);
        derCapabilityEntity.setRtgMaxW(rtgMaxWDouble);
        derCapabilityEntity.setRtgMaxWh(rtgMaxWhDouble);
        derCapabilityEntity.setRtgMinPFOverExcited(rtgMinPFOverExcitedDouble);
        derCapabilityEntity.setRtgMinPFUnderExcited(rtgMinPFUnderExcitedDouble);
        derCapabilityEntity.setRtgMinV(rtgMinVDouble);
        derCapabilityEntity.setRtgNormalCategory(rtgNormalCategoryUINT8);
        derCapabilityEntity.setRtgOverExcitedPF(rtgOverExcitedPFDouble);
        derCapabilityEntity.setRtgOverExcitedW(rtgOverExcitedWDouble);
        derCapabilityEntity.setRtgReactiveSusceptance(rtgReactiveSusceptanceDouble);
        derCapabilityEntity.setRtgUnderExcitedPF(rtgUnderExcitedPFDouble);
        derCapabilityEntity.setRtgUnderExcitedW(rtgUnderExcitedWDouble);
        derCapabilityEntity.setRtgVNom(rtgVNomDouble);
        derCapabilityEntity.setDerType(derType); 
        

    }
 
    @Transactional
    public  DERSettingsEntity createDerSettings(JSONObject payload)throws NumberFormatException, JSONException, NotFoundException {
        LOGGER.info("Received DER payload is in Der Settings " + payload); 
    
        Long derId = Long.parseLong(payload.getJSONObject("payload").getString("derId"));
        DerEntity derEntity = derRepository.findById( derId)
        .orElseThrow(() -> new NotFoundException());
        DERSettingsEntity derSettingsEntity = new DERSettingsEntity();
        derSettingsEntity.setDerEntity(derEntity);
        if (derEntity.getDerSettingsLink().equals(null)){
            String backslashUri = "/";
            String derSettingsLink = derEntity.getDerLink() + backslashUri + payload.optJSONObject("payload").optString("derSettingsLink", null);
            derSettingsEntity.setDerSettingsLink(derSettingsLink);
        } else{
            derSettingsEntity.setDerSettingsLink(derEntity.getDerSettingsLink());
        }
        try {
            derSettingsEntity  = derSettingsRepository.save(derSettingsEntity);
            derEntity.setDerSettings(derSettingsEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER Settings entity", e);
        }
        setDerSettings(derSettingsEntity  , payload);
        derSettingsEntity = derSettingsRepository.save(derSettingsEntity);
        return  derSettingsEntity;

    }
    

    /*
     * {"derSettings":"derSettings","payload":{"setESRampTms":"10","setMinPFUnderExcited":"0.95","setESHighVolt":"264","setESHighFreq":"60.5",
     * "setMaxVarNeg":"-400","setMaxWh":"2640","setGradW":"10","setMinV":"208","setMaxDischargeRateW":"480","setSoftGradW":"2","setMaxA":"250","setMaxVA":"500",
     * "setMaxChargeRateVA":"500","setMaxChargeRateW":"480","derID":"1","modesEnabled":"6","setESRandomDelay":"3","setMaxDischargeRateVA":"500","setVNom":"240",
     * "setMinPFOverExcited":"0.95","setVRef":"240","setESDelay":"5",
     * "endDeviceId":"1","setMaxW":"4800","setMaxV":"264","setESLowVolt":"208","setMaxVar":"400"},"action":"post","servicename":"createDerSettingsmanager"}
     */
    public void setDerSettings(DERSettingsEntity derEntity, JSONObject payload) {

        LOGGER.info("Received payload inside Set DER Settings : " +  payload.toString());
        JSONObject derSettingsPayload = payload.optJSONObject("payload");
        
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
                
                DERCapabilityEntity derEntity = derEntityOptional.get().getDerCapability();
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
                entityMap.put("derLink", derEntityOptional.get().getDerLink()); 
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
    
    public String getDerCapabilityHttp(Long endDeviceId, Long derId) {
    try {
        Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId, derId);
        if (derEntityOptional.isEmpty()) {
            return "<DERCapability xmlns=\"http://ieee.org/2030.5\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dercapability\">\n" +
                   "  <message>No DERCapability found for EndDevice " + endDeviceId + " and DER ID " + derId + "</message>\n" +
                   "</DERCapability>";
        }

        DerEntity derEntity = derEntityOptional.get();
        DERCapabilityEntity derCap = derEntity.getDerCapability();
        if (derCap == null) {
            return "<DERCapability xmlns=\"http://ieee.org/2030.5\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dercapability\">\n" +
                   "  <message>No DERCapability entity found for this DER</message>\n" +
                   "</DERCapability>";
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<DERCapability xmlns=\"http://ieee.org/2030.5\" ")
           .append("href=\"").append(stripHost(derCap.getDerCapabilityLink())).append("\">\n");

        // Only append each tag if the value is NOT null
        // if (derCap.getModesSupported() != null) {
        //     xml.append("  <modesSupported>").append(derCap.getModesSupported()).append("</modesSupported>\n");
        // }
        if (derCap.getRtgAbnormalCategory() != null) {
            xml.append("  <rtgAbnormalCategory>").append(derCap.getRtgAbnormalCategory()).append("</rtgAbnormalCategory>\n");
        }
        if (derCap.getRtgMaxA() != null && !derCap.getRtgMaxA().isNaN()) {
            xml.append("  <rtgMaxA>").append(derCap.getRtgMaxA()).append("</rtgMaxA>\n");
        }
        if (derCap.getRtgMaxAh() != null && !derCap.getRtgMaxAh().isNaN()) {
            xml.append("  <rtgMaxAh>").append(derCap.getRtgMaxAh()).append("</rtgMaxAh>\n");
        }
        if (derCap.getRtgMaxChargeRateVA() != null && !derCap.getRtgMaxChargeRateVA().isNaN()) {
            xml.append("  <rtgMaxChargeRateVA>").append(derCap.getRtgMaxChargeRateVA()).append("</rtgMaxChargeRateVA>\n");
        }
        if (derCap.getRtgMaxChargeRateW() != null && !derCap.getRtgMaxChargeRateW().isNaN()) {
            xml.append("  <rtgMaxChargeRateW>").append(derCap.getRtgMaxChargeRateW()).append("</rtgMaxChargeRateW>\n");
        }
        if (derCap.getRtgMaxDischargeRateVA() != null && !derCap.getRtgMaxDischargeRateVA().isNaN()) {
            xml.append("  <rtgMaxDischargeRateVA>").append(derCap.getRtgMaxDischargeRateVA()).append("</rtgMaxDischargeRateVA>\n");
        }
        if (derCap.getRtgMaxDischargeRateW() != null && !derCap.getRtgMaxDischargeRateW().isNaN()) {
            xml.append("  <rtgMaxDischargeRateW>").append(derCap.getRtgMaxDischargeRateW()).append("</rtgMaxDischargeRateW>\n");
        }
        if (derCap.getRtgMaxV() != null && !derCap.getRtgMaxV().isNaN()) {
            xml.append("  <rtgMaxV>").append(derCap.getRtgMaxV()).append("</rtgMaxV>\n");
        }
        if (derCap.getRtgMaxVA() != null  && !derCap.getRtgMaxVA().isNaN()) {
            xml.append("  <rtgMaxVA>").append(derCap.getRtgMaxVA()).append("</rtgMaxVA>\n");
        }
        if (derCap.getRtgMaxVar() != null && !derCap.getRtgMaxVar().isNaN()) {
            xml.append("  <rtgMaxVar>").append(derCap.getRtgMaxVar().longValue()).append("</rtgMaxVar>\n");
        }
        if (derCap.getRtgMaxVarNeg() != null && !derCap.getRtgMaxVarNeg().isNaN()) {
            xml.append("  <rtgMaxVarNeg>").append(derCap.getRtgMaxVarNeg().longValue()).append("</rtgMaxVarNeg>\n");
        }
        if (derCap.getRtgMaxW() != null && !derCap.getRtgMaxW().isNaN()) {
            xml.append("  <rtgMaxW>").append(derCap.getRtgMaxW().longValue()).append("</rtgMaxW>\n");
        }
        if (derCap.getRtgMaxWh() != null && !derCap.getRtgMaxWh().isNaN()) {
            xml.append("  <rtgMaxWh>").append(derCap.getRtgMaxWh().longValue()).append("</rtgMaxWh>\n");
        }
        if (derCap.getRtgMinPFOverExcited() != null && !derCap.getRtgMinPFOverExcited().isNaN()) {
            xml.append("  <rtgMinPFOverExcited>").append(String.format("%.2f", derCap.getRtgMinPFOverExcited())).append("</rtgMinPFOverExcited>\n");
        }
        if (derCap.getRtgMinPFUnderExcited() != null && !derCap.getRtgMinPFUnderExcited().isNaN()) {
            xml.append("  <rtgMinPFUnderExcited>").append(String.format("%.2f", derCap.getRtgMinPFUnderExcited())).append("</rtgMinPFUnderExcited>\n");
        }
        if (derCap.getRtgMinV() != null && !derCap.getRtgMinV().isNaN()) {
            xml.append("  <rtgMinV>").append(derCap.getRtgMinV().longValue()).append("</rtgMinV>\n");
        }
        if (derCap.getRtgNormalCategory() != null) {
            xml.append("  <rtgNormalCategory>").append(derCap.getRtgNormalCategory()).append("</rtgNormalCategory>\n");
        }
        if (derCap.getRtgOverExcitedPF() != null && !derCap.getRtgOverExcitedPF().isNaN()) {
            xml.append("  <rtgOverExcitedPF>").append(String.format("%.2f", derCap.getRtgOverExcitedPF())).append("</rtgOverExcitedPF>\n");
        }
        if (derCap.getRtgOverExcitedW() != null && !derCap.getRtgOverExcitedW().isNaN()) {
            xml.append("  <rtgOverExcitedW>").append(derCap.getRtgOverExcitedW().longValue()).append("</rtgOverExcitedW>\n");
        }
        if (derCap.getRtgReactiveSusceptance() != null && !derCap.getRtgReactiveSusceptance().isNaN()) {
            xml.append("  <rtgReactiveSusceptance>").append(derCap.getRtgReactiveSusceptance().longValue()).append("</rtgReactiveSusceptance>\n");
        }
        if (derCap.getRtgUnderExcitedPF() != null && !derCap.getRtgUnderExcitedPF().isNaN()) {
            xml.append("  <rtgUnderExcitedPF>").append(String.format("%.2f", derCap.getRtgUnderExcitedPF())).append("</rtgUnderExcitedPF>\n");
        }
        if (derCap.getRtgUnderExcitedW() != null && !derCap.getRtgUnderExcitedW().isNaN()) {
            xml.append("  <rtgUnderExcitedW>").append(derCap.getRtgUnderExcitedW().longValue()).append("</rtgUnderExcitedW>\n");
        }
        if (derCap.getRtgVNom() != null && !derCap.getRtgVNom().isNaN()) {
            xml.append("  <rtgVNom>").append(derCap.getRtgVNom().longValue()).append("</rtgVNom>\n");
        }

        // Handle derType separately
        if (derCap.getDerType() != null) {
            xml.append("  <type>").append(derCap.getDerType()).append("</type>\n");
        }

        xml.append("</DERCapability>");
        return xml.toString();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERCapability", e);
            return "<DERCapability xmlns=\"http://ieee.org/2030.5\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dercapability\">\n" +
                "  <error>Some error occurred</error>\n" +
                "</DERCapability>";
        }
    }



    public ResponseEntity<Map<String, Object>> getDerSettings(Long EndDeviceId, Long derID)

    {
        try {
            Map<String, Object> result = new HashMap<>();
            
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(EndDeviceId , derID);
    
            if (derEntityOptional.isPresent())
            {
                DERSettingsEntity derEntity = derEntityOptional.get().getDerSettings();
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

    public String getDerSettingsHttp(Long endDeviceId, Long derId) {
    try {
        Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId, derId);
        if (derEntityOptional.isEmpty()) {
            return "<DERSettings xmlns=\"http://ieee.org/2030.5\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/ders\">\n" +
                   "  <message>No DER found for EndDevice " + endDeviceId + " and DER ID " + derId + "</message>\n" +
                   "</DERSettings>";
        }
        DerEntity derEntity = derEntityOptional.get();
        DERSettingsEntity derSettings = derEntity.getDerSettings();
        if (derSettings == null) {
            return "<DERSettings xmlns=\"http://ieee.org/2030.5\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/ders\">\n" +
                   "  <message>No DERSettings entity found for this DER</message>\n" +
                   "</DERSettings>";
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<DERSettings xmlns=\"http://ieee.org/2030.5\" ")
           .append("href=\"").append(stripHost(derSettings.getDerSettingsLink())).append("\">\n");

        // Only append each tag if the value is NOT null
        // if (derSettings.getModesEnabled() != null) {
        //     xml.append("  <modesEnabled>").append(derSettings.getModesEnabled()).append("</modesEnabled>\n");
        // }
        if (derSettings.getSetESDelay() != null && !derSettings.getSetESDelay().toString().equals("NaN")) {
            xml.append("  <setESDelay>").append(derSettings.getSetESDelay()).append("</setESDelay>\n");
        }
        if (derSettings.getSetESHighFreq() != null && !derSettings.getSetESHighFreq().toString().equals("NaN")) {
            xml.append("  <setESHighFreq>").append(derSettings.getSetESHighFreq()).append("</setESHighFreq>\n");
        }
        if (derSettings.getSetESLowFreq() != null && !derSettings.getSetESLowFreq().toString().equals("NaN")) {
            xml.append("  <setESLowFreq>").append(derSettings.getSetESLowFreq()).append("</setESLowFreq>\n");
        }
        if (derSettings.getSetESHighVolt() != null && !derSettings.getSetESHighVolt().toString().equals("NaN")) {
            xml.append("  <setESHighVolt>").append(derSettings.getSetESHighVolt()).append("</setESHighVolt>\n");
        }
        if (derSettings.getSetESLowVolt() != null && !derSettings.getSetESLowVolt().toString().equals("NaN")) {
            xml.append("  <setESLowVolt>").append(derSettings.getSetESLowVolt()).append("</setESLowVolt>\n");
        }
        if (derSettings.getSetESRampTms() != null && !derSettings.getSetESRampTms().toString().equals("NaN")) {
            xml.append("  <setESRampTms>").append(derSettings.getSetESRampTms()).append("</setESRampTms>\n");
        }
        if (derSettings.getSetESRandomDelay() != null && !derSettings.getSetESRandomDelay().toString().equals("NaN")) {
            xml.append("  <setESRandomDelay>").append(derSettings.getSetESRandomDelay()).append("</setESRandomDelay>\n");
        }
        if (derSettings.getSetGradW() != null && !derSettings.getSetGradW().toString().equals("NaN")) {
            xml.append("  <setGradW>").append(derSettings.getSetGradW()).append("</setGradW>\n");
        }
        if (derSettings.getSetSoftGradW() != null && !derSettings.getSetSoftGradW().toString().equals("NaN")) {
            xml.append("  <setSoftGradW>").append(derSettings.getSetSoftGradW()).append("</setSoftGradW>\n");
        }
        if (derSettings.getSetMaxA() != null && !derSettings.getSetMaxA().toString().equals("NaN")) {
            xml.append("  <setMaxA>").append(derSettings.getSetMaxA()).append("</setMaxA>\n");
        }
        if (derSettings.getSetMaxChargeRateVA() != null && !derSettings.getSetMaxChargeRateVA().toString().equals("NaN")) {
            xml.append("  <setMaxChargeRateVA>").append(derSettings.getSetMaxChargeRateVA()).append("</setMaxChargeRateVA>\n");
        }
        if (derSettings.getSetMaxChargeRateW() != null && !derSettings.getSetMaxChargeRateW().toString().equals("NaN")) {
            xml.append("  <setMaxChargeRateW>").append(derSettings.getSetMaxChargeRateW()).append("</setMaxChargeRateW>\n");
        }
        if (derSettings.getSetMaxDischargeRateVA() != null && !derSettings.getSetMaxDischargeRateVA().toString().equals("NaN")) {
            xml.append("  <setMaxDischargeRateVA>").append(derSettings.getSetMaxDischargeRateVA()).append("</setMaxDischargeRateVA>\n");
        }
        if (derSettings.getSetMaxDischargeRateW() != null && !derSettings.getSetMaxDischargeRateW().toString().equals("NaN")) {
            xml.append("  <setMaxDischargeRateW>").append(derSettings.getSetMaxDischargeRateW()).append("</setMaxDischargeRateW>\n");
        }
        if (derSettings.getSetMaxV() != null && !derSettings.getSetMaxV().toString().equals("NaN")) {
            xml.append("  <setMaxV>").append(derSettings.getSetMaxV()).append("</setMaxV>\n");
        }
        if (derSettings.getSetMaxVA() != null && !derSettings.getSetMaxVA().toString().equals("NaN")) {
            xml.append("  <setMaxVA>").append(derSettings.getSetMaxVA()).append("</setMaxVA>\n");
        }
        if (derSettings.getSetMaxVar() != null && !derSettings.getSetMaxVar().toString().equals("NaN")) {
            xml.append("  <setMaxVar>").append(derSettings.getSetMaxVar()).append("</setMaxVar>\n");
        }
        if (derSettings.getSetMaxVarNeg() != null && !derSettings.getSetMaxVarNeg().toString().equals("NaN")) {
            xml.append("  <setMaxVarNeg>").append(derSettings.getSetMaxVarNeg()).append("</setMaxVarNeg>\n");
        }
        if (derSettings.getSetMaxW() != null && !derSettings.getSetMaxW().toString().equals("NaN")) {
            xml.append("  <setMaxW>").append(derSettings.getSetMaxW()).append("</setMaxW>\n");
        }
        if (derSettings.getSetMaxWh() != null && !derSettings.getSetMaxWh().toString().equals("NaN")) {
            xml.append("  <setMaxWh>").append(derSettings.getSetMaxWh()).append("</setMaxWh>\n");
        }
        if (derSettings.getSetMinPFOverExcited() != null && !derSettings.getSetMinPFOverExcited().toString().equals("NaN")) {
            xml.append("  <setMinPFOverExcited>").append(String.format("%.2f", derSettings.getSetMinPFOverExcited())).append("</setMinPFOverExcited>\n");
        }
        if (derSettings.getSetMinPFUnderExcited() != null && !derSettings.getSetMinPFUnderExcited().toString().equals("NaN")) {
            xml.append("  <setMinPFUnderExcited>").append(String.format("%.2f", derSettings.getSetMinPFUnderExcited())).append("</setMinPFUnderExcited>\n");
        }
        if (derSettings.getSetMinV() != null && !derSettings.getSetMinV().toString().equals("NaN")) {
            xml.append("  <setMinV>").append(derSettings.getSetMinV()).append("</setMinV>\n");
        }
        if (derSettings.getSetVNom() != null && !derSettings.getSetVNom().toString().equals("NaN")) {
            xml.append("  <setVNom>").append(derSettings.getSetVNom()).append("</setVNom>\n");
        }
        if (derSettings.getSetVRef() != null && !derSettings.getSetVRef().toString().equals("NaN")) {
            xml.append("  <setVRef>").append(derSettings.getSetVRef()).append("</setVRef>\n");
        }

        xml.append("</DERSettings>");
        return xml.toString();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERSettings", e);
            return "<DERSettings xmlns=\"http://ieee.org/2030.5\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/ders\">\n" +
                "  <error>Some error occurred</error>\n" +
                "</DERSettings>";
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
            appendIfPresent(xml, "AssociatedDERProgramListLink", derEntity.getAssociatedDERProgramListLink());
            appendIfPresent(xml, "AssociatedUsagePointLink", derEntity.getAssociatedUsagePointLink());
            appendIfPresent(xml, "CurrentDERProgramLink", derEntity.getCurrentDERProgramLink());
            appendIfPresent(xml, "DERAvailabilityLink", derEntity.getDerAvailabilityLink());
            appendIfPresent(xml, "DERCapabilityLink", derEntity.getDerCapabilityLink());
            appendIfPresent(xml, "DERSettingsLink", derEntity.getDerSettingsLink());
            appendIfPresent(xml, "DERStatusLink", derEntity.getDerStatusLink());
            
            

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
        Optional<EndDeviceEntity> endDeviceDto = endDeviceRepository.findById(endDeviceId);
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
        EndDeviceEntity endDevice = endDeviceRepository.findById( endDeviceId)
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
            entityMap.put("setMaxW", derEntity.getDerSettings().getSetMaxW());
            entityMap.put("setMaxVA", derEntity.getDerSettings().getSetMaxVA());
            LOGGER.info("DER service Power generation test is " + derEntity.getDerSettings().getSetMaxW() + " and " + derEntity.getDerSettings().getSetMaxVA());
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
        derEntity.getDerSettings().setSetMaxW(setMaxW);
        derEntity.getDerSettings().setSetMaxVA(setMaxVA);
     }

     @Transactional
     public ResponseEntity<Map<String, Object>> updateDERCapability(Long endDeviceID, Long derId, JSONObject payload)
             throws NumberFormatException, JSONException, NotFoundException {
        LOGGER.info("Received DER Capability update payload: " + payload);

        Long endDeviceId = Long.parseLong(payload.getString("endDeviceId"));
        EndDeviceEntity endDevice = endDeviceRepository.findById(endDeviceId)
                .orElseThrow(() -> new NotFoundException());

        Long derID = Long.parseLong(payload.getString("derID"));
        LOGGER.info("DER service Capability update for endDeviceId: " + endDeviceId + " and derID: " + derID);

        DerEntity derEntity = derRepository.findById(derID)
                .orElseThrow(() -> new NotFoundException());

        derEntity.setEndDevice(endDevice);

        try {
            derEntity = derRepository.save(derEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER entity", e);
        }

        // Update the capability fields
        updateDerCapabilityFields(derEntity.getDerCapability(), payload);
        derCapabilityRepository.save(derEntity.getDerCapability());

        try {
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId, derID);
            Map<String, Object> result = new HashMap<>();
            derEntity = derEntityOptional.get();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", derEntity.getId());
            entityMap.put("rtgMaxA", derEntity.getDerCapability().getRtgMaxA());
            entityMap.put("rtgMaxW", derEntity.getDerCapability().getRtgMaxW());
            LOGGER.info("DER Capability updated successfully");
            result.put("DerCapability", entityMap);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DER entity", e);
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
     }

     private void updateDerCapabilityFields(DERCapabilityEntity derCapability, JSONObject payload) {
        // Update all capability fields from the payload
        if (payload.has("rtgMaxA")) {
            derCapability.setRtgMaxA(parseDoubleFromPayload(payload, "rtgMaxA"));
        }
        if (payload.has("rtgMaxW")) {
            derCapability.setRtgMaxW(parseDoubleFromPayload(payload, "rtgMaxW"));
        }
        if (payload.has("rtgMaxVA")) {
            derCapability.setRtgMaxVA(parseDoubleFromPayload(payload, "rtgMaxVA"));
        }
        if (payload.has("rtgMaxVar")) {
            derCapability.setRtgMaxVar(parseDoubleFromPayload(payload, "rtgMaxVar"));
        }
        if (payload.has("rtgMaxVarNeg")) {
            derCapability.setRtgMaxVarNeg(parseDoubleFromPayload(payload, "rtgMaxVarNeg"));
        }
        // Add more fields as needed
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



