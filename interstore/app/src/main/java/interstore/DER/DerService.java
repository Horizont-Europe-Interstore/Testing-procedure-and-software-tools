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
    public DerEntity createDer(JSONObject payload)throws NumberFormatException, JSONException, NotFoundException  {
        LOGGER.info("Received DER payload is " + payload); 
        DerEntity derEntity = new DerEntity();
        Long endDeviceId = Long.parseLong(payload.getJSONObject("payload").getString("endDeviceId")); 
        EndDeviceDto endDevice = endDeviceRepository.findById( endDeviceId)
        .orElseThrow(() -> new NotFoundException());
        SubscribableResourceEntity subscribableResourceEntity = new SubscribableResourceEntity();
        String derListLink = endDevice.getDERListLink();
        LOGGER.info("the DER listlink is " + derListLink);
        derEntity.setEndDevice(endDevice); 
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
    public ResponseEntity<Map<String, Object>> getDerCapability(Long derID, Long EndDeviceId) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByIdAndEndDeviceId(derID, EndDeviceId);
    
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
    
   
}


