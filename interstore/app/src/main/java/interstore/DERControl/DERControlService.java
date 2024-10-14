package interstore.DERControl;

import interstore.DERCurve.DERCurveEntity;
import interstore.DERProgram.DERProgramEntity;
import interstore.DERProgram.DERProgramRepository;
import interstore.Identity.IdentifiedObjectEntity;
import interstore.Types.DERCurveType;
import interstore.Types.DERUnitRefType;
import interstore.Types.DeviceCategoryType;
import interstore.Types.PowerOfTenMultiplierType;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class DERControlService {
    private static final Logger LOGGER = Logger.getLogger(DERControlService.class.getName());

    @Autowired
    DERControlRepository derControlRepository;

    @Autowired
    DERProgramRepository derProgramRepository;

    @Autowired
    DERControlBaseRepository derControlBaseRepository;

    @Transactional
    public DERControlEntity createDERControl(JSONObject payload) throws NumberFormatException, JSONException, ChangeSetPersister.NotFoundException {
        LOGGER.info("reached here in control service class");
        DERControlEntity derControlEntity = new DERControlEntity();
        Long derProgramId = Long.parseLong(payload.getJSONObject("payload").getString("derProgramId"));
        DERProgramEntity derProgram = derProgramRepository.findById(derProgramId)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        derControlEntity.setDerProgram(derProgram);
        String derControlListLink = derProgram.getDERControlListLink();
        try {
            derControlEntity  = derControlRepository.save(derControlEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DERControl entity", e);
        }

        if(setDERControlEntity(derControlEntity  , payload,  derControlListLink )){
            derControlEntity  = derControlRepository.save(derControlEntity);
        LOGGER.info("reached here in control service class ending");
            return derControlEntity;
        }
        return null;
    }

    @Transactional
    public boolean setDERControlEntity(DERControlEntity derControlEntity, JSONObject payload, String derControlListLink) throws NumberFormatException, IllegalArgumentException, ChangeSetPersister.NotFoundException {
        Long derControlEntityId = derControlEntity.getId();
        JSONObject derControlpayload = payload.optJSONObject("payload");
        String idString = "/"+ String.valueOf(derControlEntityId) ;
        String derControlLink = derControlListLink + idString;
        String deviceCategory = derControlpayload.optString("deviceCategory");
        derControlEntity.setDerControlLink(derControlLink);
        if (DeviceCategoryType.deviceCategory.validateDeviceCategory(deviceCategory)){
            derControlEntity.setDeviceCategory(Integer.parseInt(derControlpayload.optString("deviceCategory")));
        }
        else {
            LOGGER.log(Level.SEVERE, "Incorrect DeviceCategoryType Provided");
            throw new IllegalArgumentException("Invalid DeviceCategoryType provided: " + derControlpayload.optString("deviceCategory"));
        }

        DERControlBase derControlBase = new DERControlBase();
        return setEntityValues(derControlpayload, derControlEntity, derControlBase);
    }

    @Transactional
    public boolean setEntityValues(JSONObject payload, DERControlEntity entity, DERControlBase derControlBase) {
        try {
            Iterator<String> keys = payload.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = payload.optString(key, "");
                if (!value.isEmpty()) {
                    if (key.equals("opModConnect")) {
                        derControlBase.setOpModConnect(payload.optBoolean(key, false));
                    } else if (key.equals("opModEnergize")) {
                        derControlBase.setOpModEnergize(payload.optBoolean(key, false));
                    } else if (key.equals("opModFixedPFAbsorbW")) {
                        derControlBase.setOpModFixedPFAbsorbW(payload.optInt(key, 0));
                    } else if (key.equals("opModFixedPFInjectW")) {
                        derControlBase.setOpModFixedPFInjectW(payload.optInt(key, 0));
                    } else if (key.equals("opModFixedVar")) {
                        derControlBase.setOpModFixedVar(payload.optInt(key, 0));
                    } else if (key.equals("opModFixedW")) {
                        derControlBase.setOpModFixedW(payload.optInt(key, 0));
                    } else if (key.equals("opModFreqDroop")) {
                        derControlBase.setOpModFreqDroop(payload.optInt(key, 0));
                    } else if (key.equals("opModFreqWatt")) {
                        derControlBase.setOpModFreqWatt(payload.optString(key, ""));
                    } else if (key.equals("opModHFRTMayTrip")) {
                        derControlBase.setOpModHFRTMayTrip(payload.optString(key, ""));
                    } else if (key.equals("opModHFRTMustTrip")) {
                        derControlBase.setOpModHFRTMustTrip(payload.optString(key, ""));
                    } else if (key.equals("opModHVRTMayTrip")) {
                        derControlBase.setOpModHVRTMayTrip(payload.optString(key, ""));
                    } else if (key.equals("opModHVRTMomentaryCessation")) {
                        derControlBase.setOpModHVRTMomentaryCessation(payload.optString(key, ""));
                    } else if (key.equals("opModHVRTMustTrip")) {
                        derControlBase.setOpModHVRTMustTrip(payload.optString(key, ""));
                    } else if (key.equals("opModLFRTMayTrip")) {
                        derControlBase.setOpModLFRTMayTrip(payload.optString(key, ""));
                    } else if (key.equals("opModLFRTMustTrip")) {
                        derControlBase.setOpModLFRTMustTrip(payload.optString(key, ""));
                    } else if (key.equals("opModLVRTMayTrip")) {
                        derControlBase.setOpModLVRTMayTrip(payload.optString(key, ""));
                    } else if (key.equals("opModLVRTMomentaryCessation")) {
                        derControlBase.setOpModLVRTMomentaryCessation(payload.optString(key, ""));
                    } else if (key.equals("opModLVRTMustTrip")) {
                        derControlBase.setOpModLVRTMustTrip(payload.optString(key, ""));
                    } else if (key.equals("IntegeropModMaxLimW")) {
                        derControlBase.setIntegeropModMaxLimW(payload.optInt(key, 0));
                    } else if (key.equals("opModTargetVar")) {
                        derControlBase.setOpModTargetVar(payload.optInt(key, 0));
                    } else if (key.equals("opModTargetW")) {
                        derControlBase.setOpModTargetW(payload.optInt(key, 0));
                    } else if (key.equals("opModVoltVar")) {
                        derControlBase.setOpModVoltVar(payload.optString(key, ""));
                    } else if (key.equals("opModVoltWatt")) {
                        derControlBase.setOpModVoltWatt(payload.optString(key, ""));
                    } else if (key.equals("opModWattPF")) {
                        derControlBase.setOpModWattPF(payload.optString(key, ""));
                    } else if (key.equals("opModWattVar")) {
                        derControlBase.setOpModWattVar(payload.optString(key, ""));
                    } else if (key.equals("rampTms")) {
                        derControlBase.setRampTms(payload.optInt(key, 0));
                    }
                }
            }

            derControlBaseRepository.save(derControlBase);
            entity.setDer_control_base(derControlBase);
            derControlRepository.save(entity);
            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting entity values", e);
            return false;
        }
    }

    public ResponseEntity<Map<String, Object>> getAllDERControls(Long derProgramId) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<DERControlEntity> derControlEntityList = derControlRepository.findByDerProgramId(derProgramId);

            List<Map<String, Object>> derControlDetails = derControlEntityList.stream()
                    .map(derControlEntity -> {
                        Map<String, Object> entityMap = new HashMap<>();
                        entityMap.put("id", derControlEntity.getId());
                        entityMap.put("deviceCategory", derControlEntity.getDeviceCategory() != null ? derControlEntity.getDeviceCategory() : "N/A");
                        entityMap.put("derControlLink", derControlEntity.getDerControlLink() != null ? derControlEntity.getDerControlLink() : "N/A");

                        // Map DERControlBase details
                        DERControlBase derControlBase = derControlEntity.getDer_control_base();
                        if (derControlBase != null) {
                            Map<String, Object> baseMap = new HashMap<>();
                            baseMap.put("opModConnect", derControlBase.getOpModConnect());
                            baseMap.put("opModEnergize", derControlBase.getOpModEnergize());
                            baseMap.put("opModFixedPFAbsorbW", derControlBase.getOpModFixedPFAbsorbW());
                            baseMap.put("opModFixedPFInjectW", derControlBase.getOpModFixedPFInjectW());
                            baseMap.put("opModFixedVar", derControlBase.getOpModFixedVar());
                            baseMap.put("opModFixedW", derControlBase.getOpModFixedW());
                            baseMap.put("opModFreqDroop", derControlBase.getOpModFreqDroop());
                            baseMap.put("opModFreqWatt", derControlBase.getOpModFreqWatt());
                            baseMap.put("opModHFRTMayTrip", derControlBase.getOpModHFRTMayTrip());
                            baseMap.put("opModHFRTMustTrip", derControlBase.getOpModHFRTMustTrip());
                            baseMap.put("opModHVRTMayTrip", derControlBase.getOpModHVRTMayTrip());
                            baseMap.put("opModHVRTMomentaryCessation", derControlBase.getOpModHVRTMomentaryCessation());
                            baseMap.put("opModHVRTMustTrip", derControlBase.getOpModHVRTMustTrip());
                            baseMap.put("opModLFRTMayTrip", derControlBase.getOpModLFRTMayTrip());
                            baseMap.put("opModLFRTMustTrip", derControlBase.getOpModLFRTMustTrip());
                            baseMap.put("opModLVRTMayTrip", derControlBase.getOpModLVRTMayTrip());
                            baseMap.put("opModLVRTMomentaryCessation", derControlBase.getOpModLVRTMomentaryCessation());
                            baseMap.put("opModLVRTMustTrip", derControlBase.getOpModLVRTMustTrip());
                            baseMap.put("opModMaxLimW", derControlBase.getIntegeropModMaxLimW());
                            baseMap.put("opModTargetVar", derControlBase.getOpModTargetVar());
                            baseMap.put("opModTargetW", derControlBase.getOpModTargetW());
                            baseMap.put("opModVoltVar", derControlBase.getOpModVoltVar());
                            baseMap.put("opModVoltWatt", derControlBase.getOpModVoltWatt());
                            baseMap.put("opModWattPF", derControlBase.getOpModWattPF());
                            baseMap.put("opModWattVar", derControlBase.getOpModWattVar());
                            baseMap.put("rampTms", derControlBase.getRampTms());

                            entityMap.put("derControlBase", baseMap);
                        } else {
                            entityMap.put("derControlBase", "N/A");
                        }

                        return entityMap;
                    })
                    .collect(Collectors.toList());

            if (derControlDetails.isEmpty()) {
                responseMap.put("message", "No DERControls found.");
            } else {
                responseMap.put("derControls", derControlDetails);
            }

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERControls", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    public ResponseEntity<Map<String, Object>> getDERControl(Long derProgramId, Long derControlId) {
        try {
            Map<String, Object> result = new HashMap<>();
            Optional<DERControlEntity> derControlEntityOptional = derControlRepository.findFirstByDerProgramIdAndId(derProgramId, derControlId);

            if (derControlEntityOptional.isEmpty()) {
                result.put("message", "No DERControl found for DERProgram " + derProgramId + " and DERControl ID " + derControlId);
                return ResponseEntity.ok(result);
            }

            DERControlEntity derControlEntity = derControlEntityOptional.get();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", derControlEntity.getId());
            entityMap.put("deviceCategory", derControlEntity.getDeviceCategory() != null ? derControlEntity.getDeviceCategory() : "N/A");
            entityMap.put("derControlLink", derControlEntity.getDerControlLink() != null ? derControlEntity.getDerControlLink() : "N/A");

            // Map DERControlBase details
            DERControlBase derControlBase = derControlEntity.getDer_control_base();
            if (derControlBase != null) {
                Map<String, Object> baseMap = new HashMap<>();
                baseMap.put("opModConnect", derControlBase.getOpModConnect());
                baseMap.put("opModEnergize", derControlBase.getOpModEnergize());
                baseMap.put("opModFixedPFAbsorbW", derControlBase.getOpModFixedPFAbsorbW());
                baseMap.put("opModFixedPFInjectW", derControlBase.getOpModFixedPFInjectW());
                baseMap.put("opModFixedVar", derControlBase.getOpModFixedVar());
                baseMap.put("opModFixedW", derControlBase.getOpModFixedW());
                baseMap.put("opModFreqDroop", derControlBase.getOpModFreqDroop());
                baseMap.put("opModFreqWatt", derControlBase.getOpModFreqWatt());
                baseMap.put("opModHFRTMayTrip", derControlBase.getOpModHFRTMayTrip());
                baseMap.put("opModHFRTMustTrip", derControlBase.getOpModHFRTMustTrip());
                baseMap.put("opModHVRTMayTrip", derControlBase.getOpModHVRTMayTrip());
                baseMap.put("opModHVRTMomentaryCessation", derControlBase.getOpModHVRTMomentaryCessation());
                baseMap.put("opModHVRTMustTrip", derControlBase.getOpModHVRTMustTrip());
                baseMap.put("opModLFRTMayTrip", derControlBase.getOpModLFRTMayTrip());
                baseMap.put("opModLFRTMustTrip", derControlBase.getOpModLFRTMustTrip());
                baseMap.put("opModLVRTMayTrip", derControlBase.getOpModLVRTMayTrip());
                baseMap.put("opModLVRTMomentaryCessation", derControlBase.getOpModLVRTMomentaryCessation());
                baseMap.put("opModLVRTMustTrip", derControlBase.getOpModLVRTMustTrip());
                baseMap.put("opModMaxLimW", derControlBase.getIntegeropModMaxLimW());
                baseMap.put("opModTargetVar", derControlBase.getOpModTargetVar());
                baseMap.put("opModTargetW", derControlBase.getOpModTargetW());
                baseMap.put("opModVoltVar", derControlBase.getOpModVoltVar());
                baseMap.put("opModVoltWatt", derControlBase.getOpModVoltWatt());
                baseMap.put("opModWattPF", derControlBase.getOpModWattPF());
                baseMap.put("opModWattVar", derControlBase.getOpModWattVar());
                baseMap.put("rampTms", derControlBase.getRampTms());

                entityMap.put("derControlBase", baseMap);
            } else {
                entityMap.put("derControlBase", "N/A");
            }

            result.put("DERControl", entityMap);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERControl", e);
            return ResponseEntity.status(404).body(null);
        }
    }

}
