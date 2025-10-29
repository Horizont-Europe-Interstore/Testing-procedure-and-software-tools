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
import org.springframework.data.crossstore.ChangeSetPersister;
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
        String mRID = derControlpayload.optString("mRID");
        String mRIDString = HexBinary128.validateAndFormatHexValue(mRID);
        mRIDType mRIDValue = new mRIDType(mRIDString);
        String version = derControlpayload.optString("version");
        int intVersion = Integer.parseInt(version);
        String description = derControlpayload.optString("description");
        String currentStatus = derControlpayload.optString("currentStatus");
        String dateTime = derControlpayload.optString("dateTime");
        String potentiallySuperseded = derControlpayload.optString("potentiallySuperseded");
        String duration = derControlpayload.optString("duration");
        String start = derControlpayload.optString("start");
        String randomizeDuration = derControlpayload.optString("randomizeDuration");
        String randomizeStart = derControlpayload.optString("randomizeStart");

        derControlEntity.setDuration(Integer.parseInt(duration));
        derControlEntity.setStart(Integer.parseInt(start));
        derControlEntity.setRandomizeDuration(Integer.parseInt(randomizeDuration));
        derControlEntity.setRandomizeStart(Integer.parseInt(randomizeStart));
        EventStatusEntity eventStatusEntity = new EventStatusEntity();
        eventStatusEntity.setCurrentStatus(Integer.parseInt(currentStatus));
        eventStatusEntity.setDateTime(dateTime);
        eventStatusEntity.setPotentiallySuperseded(Boolean.parseBoolean(potentiallySuperseded));
        eventStatusRepository.save(eventStatusEntity);
        derControlEntity.setEventStatusEntity(eventStatusEntity);
        derControlEntity.setDerControlLink(derControlLink);
        long currentTime =  Instant.now().getEpochSecond();
        derControlEntity.setCreationTime(String.valueOf(currentTime));
        RespondableSubscribableIdentifiedObjectEntity respondableSubscribableIdentifiedObjectEntity = new RespondableSubscribableIdentifiedObjectEntity();
        respondableSubscribableIdentifiedObjectEntity.setmRID(mRIDValue.toString());
        respondableSubscribableIdentifiedObjectEntity.setDescription(description);
        respondableSubscribableIdentifiedObjectEntity.setVersion(intVersion);
        respondableSubscribableIdentifiedObjectRepository.save(respondableSubscribableIdentifiedObjectEntity);
        derControlEntity.setRespondableSubscribableIdentifiedObjectEntity(respondableSubscribableIdentifiedObjectEntity);
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
                                    // Convert method name like getOpModVoltWatt -> opModVoltWatt
                                    String fieldName = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);

                                    // Strings are usually hrefs, Booleans/Integers are direct values
                                    if (value instanceof String) {
                                        xml.append("   <").append(fieldName).append(" href=\"")
                                        .append(stripHost((String) value))
                                        .append("\"/>\n");
                                    }
                                }
                            }
                        }
                        // Add more fields as needed, similar to your DERControlBase mapping
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
