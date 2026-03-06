package interstore.DERControl;

import interstore.DERProgram.DERProgramEntity;
import interstore.DERProgram.DERProgramRepository;
import interstore.Events.EventStatusEntity;
import interstore.Events.EventStatusRepository;
import interstore.Identity.RespondableSubscribableIdentifiedObjectEntity;
import interstore.Identity.RespondableSubscribableIdentifiedObjectRepository;
import interstore.Types.DeviceCategoryType;
import interstore.Types.HexBinary128;
import interstore.Types.mRIDType;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.lang.reflect.Method;
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

    @Autowired
    RespondableSubscribableIdentifiedObjectRepository respondableSubscribableIdentifiedObjectRepository;

    @Autowired
    EventStatusRepository eventStatusRepository;

    @Transactional
    public DERControlEntity createDERControl(JSONObject payload) throws NumberFormatException, JSONException {
        LOGGER.info("reached here in control service class");
        DERControlEntity derControlEntity = new DERControlEntity();
        Long derProgramId = Long.parseLong(payload.getJSONObject("payload").getString("derProgramId"));
        DERProgramEntity derProgram = derProgramRepository.findById(derProgramId)
                .orElseThrow(() -> new RuntimeException("DERProgram not found"));
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
    public boolean setDERControlEntity(DERControlEntity derControlEntity, JSONObject payload, String derControlListLink) throws NumberFormatException, IllegalArgumentException {
        Long derControlEntityId = derControlEntity.getId();
        JSONObject derControlpayload = payload.optJSONObject("payload");
        if (derControlpayload == null) {
            throw new IllegalArgumentException("Missing 'payload' object in request");
        }
        
        String idString = "/"+ String.valueOf(derControlEntityId);
        String derControlLink = derControlListLink + idString;
        String deviceCategory = derControlpayload.optString("deviceCategory", "");
        String mRID = derControlpayload.optString("mRID", "");
        String version = derControlpayload.optString("version", "0");
        String description = derControlpayload.optString("description", "");
        String currentStatus = derControlpayload.optString("currentStatus", "0");
        String dateTime = derControlpayload.optString("dateTime", String.valueOf(Instant.now().getEpochSecond()));
        String potentiallySuperseded = derControlpayload.optString("potentiallySuperseded", "false");
        String duration = derControlpayload.optString("duration", "0");
        String start = derControlpayload.optString("start", "0");
        String randomizeDuration = derControlpayload.optString("randomizeDuration", "0");
        String randomizeStart = derControlpayload.optString("randomizeStart", "0");

        try {
            derControlEntity.setDuration(Integer.parseInt(duration));
            derControlEntity.setStart(Integer.parseInt(start));
            derControlEntity.setRandomizeDuration(Integer.parseInt(randomizeDuration));
            derControlEntity.setRandomizeStart(Integer.parseInt(randomizeStart));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for duration/start/randomize fields: " + e.getMessage());
        }
        
        EventStatusEntity eventStatusEntity = new EventStatusEntity();
        try {
            eventStatusEntity.setCurrentStatus(Integer.parseInt(currentStatus));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid currentStatus value: " + currentStatus);
        }
        eventStatusEntity.setDateTime(dateTime);
        eventStatusEntity.setPotentiallySuperseded(Boolean.parseBoolean(potentiallySuperseded));
        eventStatusRepository.save(eventStatusEntity);
        derControlEntity.setEventStatusEntity(eventStatusEntity);
        derControlEntity.setDerControlLink(derControlLink);
        long currentTime = Instant.now().getEpochSecond();
        derControlEntity.setCreationTime(String.valueOf(currentTime));
        
        RespondableSubscribableIdentifiedObjectEntity respondableSubscribableIdentifiedObjectEntity = new RespondableSubscribableIdentifiedObjectEntity();
        if (!mRID.isEmpty()) {
            String mRIDString = HexBinary128.validateAndFormatHexValue(mRID);
            mRIDType mRIDValue = new mRIDType(mRIDString);
            respondableSubscribableIdentifiedObjectEntity.setmRID(mRIDValue.toString());
        }
        respondableSubscribableIdentifiedObjectEntity.setDescription(description);
        try {
            respondableSubscribableIdentifiedObjectEntity.setVersion(Integer.parseInt(version));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid version value: " + version);
        }
        respondableSubscribableIdentifiedObjectRepository.save(respondableSubscribableIdentifiedObjectEntity);
        derControlEntity.setRespondableSubscribableIdentifiedObjectEntity(respondableSubscribableIdentifiedObjectEntity);
        
        if (!deviceCategory.isEmpty()) {
            if (DeviceCategoryType.deviceCategory.validateDeviceCategory(deviceCategory)) {
                try {
                    derControlEntity.setDeviceCategory(Integer.parseInt(deviceCategory));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid deviceCategory number format: " + deviceCategory);
                }
            } else {
                LOGGER.log(Level.SEVERE, "Incorrect DeviceCategoryType Provided");
                throw new IllegalArgumentException("Invalid DeviceCategoryType provided: " + deviceCategory);
            }
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
                if (!payload.isNull(key)) {
                    switch (key) {
                        case "opModChargeMode":
                        case "opModDischargeMode":
                        case "opModConnect":
                        case "opModEnergize":
                            if (payload.get(key) instanceof Boolean) {
                                switch (key) {
                                    case "opModChargeMode": derControlBase.setOpModChargeMode(payload.getBoolean(key)); break;
                                    case "opModDischargeMode": derControlBase.setOpModDischargeMode(payload.getBoolean(key)); break;
                                    case "opModConnect": derControlBase.setOpModConnect(payload.getBoolean(key)); break;
                                    case "opModEnergize": derControlBase.setOpModEnergize(payload.getBoolean(key)); break;
                                }
                            }
                            break;
                        case "opModFixedPFAbsorbW":
                        case "opModFixedPFInjectW":
                        case "opModFixedVar":
                        case "opModFixedW":
                        case "opModFreqDroop":
                        case "IntegeropModMaxLimW":
                        case "opModTargetVar":
                        case "opModTargetW":
                        case "rampTms":
                            Object intValue = payload.get(key);
                            if (intValue instanceof Number) {
                                int val = ((Number) intValue).intValue();
                                switch (key) {
                                    case "opModFixedPFAbsorbW": derControlBase.setOpModFixedPFAbsorbW(val); break;
                                    case "opModFixedPFInjectW": derControlBase.setOpModFixedPFInjectW(val); break;
                                    case "opModFixedVar": derControlBase.setOpModFixedVar(val); break;
                                    case "opModFixedW": derControlBase.setOpModFixedW(val); break;
                                    case "opModFreqDroop": derControlBase.setOpModFreqDroop(val); break;
                                    case "IntegeropModMaxLimW": derControlBase.setIntegeropModMaxLimW(val); break;
                                    case "opModTargetVar": derControlBase.setOpModTargetVar(val); break;
                                    case "opModTargetW": derControlBase.setOpModTargetW(val); break;
                                    case "rampTms": derControlBase.setRampTms(val); break;
                                }
                            }
                            break;
                        case "opModFreqWatt":
                        case "opModHFRTMayTrip":
                        case "opModHFRTMustTrip":
                        case "opModHVRTMayTrip":
                        case "opModHVRTMomentaryCessation":
                        case "opModHVRTMustTrip":
                        case "opModLFRTMayTrip":
                        case "opModLFRTMustTrip":
                        case "opModLVRTMayTrip":
                        case "opModLVRTMomentaryCessation":
                        case "opModLVRTMustTrip":
                        case "opModVoltVar":
                        case "opModVoltWatt":
                        case "opModWattPF":
                        case "opModWattVar":
                            String strValue = payload.getString(key);
                            if (strValue != null && !strValue.isEmpty()) {
                                switch (key) {
                                    case "opModFreqWatt": derControlBase.setOpModFreqWatt(strValue); break;
                                    case "opModHFRTMayTrip": derControlBase.setOpModHFRTMayTrip(strValue); break;
                                    case "opModHFRTMustTrip": derControlBase.setOpModHFRTMustTrip(strValue); break;
                                    case "opModHVRTMayTrip": derControlBase.setOpModHVRTMayTrip(strValue); break;
                                    case "opModHVRTMomentaryCessation": derControlBase.setOpModHVRTMomentaryCessation(strValue); break;
                                    case "opModHVRTMustTrip": derControlBase.setOpModHVRTMustTrip(strValue); break;
                                    case "opModLFRTMayTrip": derControlBase.setOpModLFRTMayTrip(strValue); break;
                                    case "opModLFRTMustTrip": derControlBase.setOpModLFRTMustTrip(strValue); break;
                                    case "opModLVRTMayTrip": derControlBase.setOpModLVRTMayTrip(strValue); break;
                                    case "opModLVRTMomentaryCessation": derControlBase.setOpModLVRTMomentaryCessation(strValue); break;
                                    case "opModLVRTMustTrip": derControlBase.setOpModLVRTMustTrip(strValue); break;
                                    case "opModVoltVar": derControlBase.setOpModVoltVar(strValue); break;
                                    case "opModVoltWatt": derControlBase.setOpModVoltWatt(strValue); break;
                                    case "opModWattPF": derControlBase.setOpModWattPF(strValue); break;
                                    case "opModWattVar": derControlBase.setOpModWattVar(strValue); break;
                                }
                            }
                            break;
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
                responseMap.put("DERControls", derControlDetails);
            }

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERControls", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    public String getAllDERControlsHttp(Long derProgramId){
        try {
            List<DERControlEntity> derControlEntityList = derControlRepository.findByDerProgramId(derProgramId);
            String derControlListHref = stripHost(derProgramRepository.findById(derProgramId)
                    .orElseThrow(() -> new RuntimeException("DERProgram not found"))
                    .getDERControlListLink());

            StringBuilder xml = new StringBuilder();
            xml.append("<DERControlList xmlns=\"http://ieee.org/2030.5\" ")
            .append("href=\"").append(derControlListHref).append("\" ")
            .append("all=\"").append(derControlEntityList.size()).append("\" ")
            .append("results=\"").append(derControlEntityList.size()).append("\" ")
            .append("subscribable=\"1\">\n");

            if (derControlEntityList.isEmpty()) {
                xml.append(" <message>No DERControls found for DERProgram ")
                .append(derProgramId).append("</message>\n");
            } else {
                for (DERControlEntity derControl : derControlEntityList) {
                    RespondableSubscribableIdentifiedObjectEntity identifiedObject = derControl.getRespondableSubscribableIdentifiedObjectEntity();
                    DERControlBase derControlBase = derControl.getDer_control_base();

                    xml.append(" <DERControl href=\"").append(stripHost(derControl.getDerControlLink())).append("\">\n");

                    // IdentifiedObject attributes
                    xml.append("  <mRID>")
                    .append(identifiedObject != null && identifiedObject.getmRID() != null ? identifiedObject.getmRID() : "N/A")
                    .append("</mRID>\n");

                    xml.append("  <description>")
                    .append(identifiedObject != null && identifiedObject.getDescription() != null ? identifiedObject.getDescription() : "No description")
                    .append("</description>\n");

                    // Creation time
                    xml.append("  <creationTime>")
                    .append(derControl.getCreationTime() != null ? derControl.getCreationTime() : "0")
                    .append("</creationTime>\n");

                    // EventStatus section (you can adapt this with your event data if available)
                    xml.append("  <EventStatus>\n");
                    xml.append("   <currentStatus>")
                    .append(derControl.getEventStatusEntity().getCurrentStatus() != null ? derControl.getEventStatusEntity().getCurrentStatus() : "1")
                    .append("</currentStatus>\n");
                    xml.append("   <dateTime>")
                    .append(derControl.getEventStatusEntity().getDateTime() != null ? derControl.getEventStatusEntity().getDateTime() : "0")
                    .append("</dateTime>\n");
                    xml.append("   <potentiallySuperseded>")
                    .append(derControl.getEventStatusEntity().getPotentiallySuperseded())
                    .append("</potentiallySuperseded>\n");
                    xml.append("  </EventStatus>\n");

                    // Interval
                    xml.append("  <interval>\n");
                    xml.append("   <duration>")
                    .append(derControl.getDuration() != null ? derControl.getDuration() : "0")
                    .append("</duration>\n");
                    xml.append("   <start>")
                    .append(derControl.getStart() != null ? derControl.getStart() : "0")
                    .append("</start>\n");
                    xml.append("  </interval>\n");

                    // Randomization parameters
                    xml.append("  <randomizeDuration>")
                    .append(derControl.getRandomizeDuration() != null ? derControl.getRandomizeDuration() : "180")
                    .append("</randomizeDuration>\n");
                    xml.append("  <randomizeStart>")
                    .append(derControl.getRandomizeStart() != null ? derControl.getRandomizeStart() : "180")
                    .append("</randomizeStart>\n");

                    // DERControlBase
                    xml.append("  <DERControlBase>\n");
                    if (derControlBase != null) {
                        for (Method method : DERControlBase.class.getMethods()) {
                            if (method.getName().startsWith("get") && !method.getName().equals("getId")) {
                                Object value = method.invoke(derControlBase);

                                if (value != null) {
                                    String fieldName = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);

                                    if (value instanceof String) {
                                        xml.append("   <").append(fieldName).append(" href=\"")
                                        .append(stripHost((String) value))
                                        .append("\"/>\n");
                                    } else if (value instanceof Boolean) {
                                        xml.append("   <").append(fieldName).append(">")
                                        .append(value)
                                        .append("</").append(fieldName).append(">\n");
                                    } else if (value instanceof Integer) {
                                        xml.append("   <").append(fieldName).append(">")
                                        .append(value)
                                        .append("</").append(fieldName).append(">\n");
                                    }
                                }
                            }
                        }
                    } else {
                        xml.append("   <message>No DERControlBase data</message>\n");
                    }
                    xml.append("  </DERControlBase>\n");

                    xml.append(" </DERControl>\n");
                }
            }

            xml.append("</DERControlList>");
            return xml.toString();

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error retrieving DERControls", e);
                return "<DERControlList xmlns=\"http://ieee.org/2030.5\" href=\"/derp/" + derProgramId + "/derc\" all=\"0\" results=\"0\">\n" +
                    "<error>Some error occurred</error>\n" +
                    "</DERControlList>";
            }
    }

    public String getDERControlHttp(Long derProgramId, Long derControlId) {
        try {
            Optional<DERControlEntity> derControlEntityOptional = derControlRepository.findFirstByDerProgramIdAndId(derProgramId, derControlId);

            if (derControlEntityOptional.isEmpty()) {
                return "<DERControl xmlns=\"http://ieee.org/2030.5\">\n" +
                       "<error>No DERControl found for DERProgram " + derProgramId + " and DERControl ID " + derControlId + "</error>\n" +
                       "</DERControl>";
            }

            DERControlEntity derControl = derControlEntityOptional.get();
            RespondableSubscribableIdentifiedObjectEntity identifiedObject = derControl.getRespondableSubscribableIdentifiedObjectEntity();
            DERControlBase derControlBase = derControl.getDer_control_base();

            StringBuilder xml = new StringBuilder();
            xml.append("<DERControl xmlns=\"http://ieee.org/2030.5\" href=\"").append(stripHost(derControl.getDerControlLink())).append("\">\n");

            xml.append(" <mRID>")
               .append(identifiedObject != null && identifiedObject.getmRID() != null ? identifiedObject.getmRID() : "N/A")
               .append("</mRID>\n");

            xml.append(" <description>")
               .append(identifiedObject != null && identifiedObject.getDescription() != null ? identifiedObject.getDescription() : "No description")
               .append("</description>\n");

            xml.append(" <creationTime>")
               .append(derControl.getCreationTime() != null ? derControl.getCreationTime() : "0")
               .append("</creationTime>\n");

            xml.append(" <EventStatus>\n");
            xml.append("  <currentStatus>")
               .append(derControl.getEventStatusEntity().getCurrentStatus() != null ? derControl.getEventStatusEntity().getCurrentStatus() : "1")
               .append("</currentStatus>\n");
            xml.append("  <dateTime>")
               .append(derControl.getEventStatusEntity().getDateTime() != null ? derControl.getEventStatusEntity().getDateTime() : "0")
               .append("</dateTime>\n");
            xml.append("  <potentiallySuperseded>")
               .append(derControl.getEventStatusEntity().getPotentiallySuperseded())
               .append("</potentiallySuperseded>\n");
            xml.append(" </EventStatus>\n");

            xml.append(" <interval>\n");
            xml.append("  <duration>")
               .append(derControl.getDuration() != null ? derControl.getDuration() : "0")
               .append("</duration>\n");
            xml.append("  <start>")
               .append(derControl.getStart() != null ? derControl.getStart() : "0")
               .append("</start>\n");
            xml.append(" </interval>\n");

            xml.append(" <randomizeDuration>")
               .append(derControl.getRandomizeDuration() != null ? derControl.getRandomizeDuration() : "180")
               .append("</randomizeDuration>\n");
            xml.append(" <randomizeStart>")
               .append(derControl.getRandomizeStart() != null ? derControl.getRandomizeStart() : "180")
               .append("</randomizeStart>\n");

            xml.append(" <DERControlBase>\n");
            if (derControlBase != null) {
                for (Method method : DERControlBase.class.getMethods()) {
                    if (method.getName().startsWith("get") && !method.getName().equals("getId")) {
                        Object value = method.invoke(derControlBase);
                        if (value != null) {
                            String fieldName = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);
                            if (value instanceof String) {
                                xml.append("  <").append(fieldName).append(" href=\"")
                                   .append(stripHost((String) value))
                                   .append("\"/>\n");
                            } else if (value instanceof Boolean) {
                                xml.append("  <").append(fieldName).append(">")
                                   .append(value)
                                   .append("</").append(fieldName).append(">\n");
                            } else if (value instanceof Integer) {
                                xml.append("  <").append(fieldName).append(">")
                                   .append(value)
                                   .append("</").append(fieldName).append(">\n");
                            }
                        }
                    }
                }
            } else {
                xml.append("  <message>No DERControlBase data</message>\n");
            }
            xml.append(" </DERControlBase>\n");

            xml.append("</DERControl>");
            return xml.toString();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERControl", e);
            return "<DERControl xmlns=\"http://ieee.org/2030.5\">\n" +
                   "<error>Error occurred: " + e.getMessage() + "</error>\n" +
                   "</DERControl>";
        }
    }

    public ResponseEntity<Map<String, Object>> getDERControl(Long derProgramId, Long derControlId) {
        LOGGER.info("Reached in get a single der control service class");
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
            LOGGER.info("Leaving get a single der control service class");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERControl", e);
            return ResponseEntity.status(404).body(null);
        }
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

}
