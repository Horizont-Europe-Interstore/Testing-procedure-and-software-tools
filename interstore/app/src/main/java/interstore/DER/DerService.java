package interstore.DER;
import interstore.DER.DERCapability.DERCapabilityEntity;
import interstore.DER.DERCapability.DERCapabilityRepository;
import interstore.DER.DERSettings.DERSettingsEntity;
import interstore.DER.DERSettings.DERSettingsRepository;
import interstore.DER.DERStatus.DERStatusEntity;
import interstore.DER.DERStatus.DERStatusRepository;
import interstore.DER.DERAvailabilty.DERAvailabilityEntity;
import interstore.DER.DERAvailabilty.DERAvailabilityRepository;
import interstore.EndDevice.EndDeviceEntity;
import interstore.EndDevice.EndDeviceRepository;
import interstore.Identity.SubscribableResourceEntity;
import interstore.Identity.SubscribableResourceRepository;
import interstore.Types.DERType; 
import interstore.DER.DERCapability.DERCapabilityEntity.XmlvalueAndMultiplier;
import interstore.DER.DERCapability.DERCapabilityEntity.XmlDisplacementAndMultiplier;
import interstore.DER.DERSettings.DERSettingsEntity.XmlvalueAndMultiplierSettings;
import interstore.DER.DERSettings.DERSettingsEntity.XmlDisplacementAndMultiplierSettings; 
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.IntegerConversion;
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

// XmlvalueAndMultiplierSettings

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

    @Autowired
    private DERStatusRepository derStatusRepository;

    @Autowired
    private DERAvailabilityRepository derAvailabilityRepository;

    
    /* the necessary thing to create the DER is that end device id this end device id shall fetch the 
    corresponding object from the database and set the end device object to the DER entity , while 
    creation of the end device the der link is added so this can be extracted */
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
     /* the der capability link and der settings shoulld be present  */
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

    
    

    public XmlvalueAndMultiplier derCapablityExtractor(JSONObject Derpayload, String rtg)
    {
          if(!Derpayload.has(rtg))
          {
            return null;
          }
       JSONObject fieldOJsonObject = Derpayload.getJSONObject(rtg);
       Integer value = fieldOJsonObject.has("value") ? parseIntValue(fieldOJsonObject, "value"):null;
       Integer multiplier = fieldOJsonObject.has("multiplier") ? fieldOJsonObject.getInt("multiplier"):null;
       return new XmlvalueAndMultiplier(multiplier, value);

    }
    

    public XmlDisplacementAndMultiplier derDisplacementExtractor(JSONObject derPayload, String rtg) {
        if (!derPayload.has(rtg)) {
            return null;
        }
        JSONObject fieldObject = derPayload.getJSONObject(rtg);
        Integer displacement = fieldObject.has("displacement") ? fieldObject.getInt("displacement") : null;
        Integer multiplier = fieldObject.has("multiplier") ? fieldObject.getInt("multiplier") : null;
        return new XmlDisplacementAndMultiplier(displacement, multiplier);
    }

  
    public XmlvalueAndMultiplierSettings derSettingsExtractor(JSONObject derPayload, String rtg)
    {
         if(!derPayload.has(rtg))
         {
           return null;
         }
        JSONObject fieldOJsonObject = derPayload.getJSONObject(rtg);
        Integer value = fieldOJsonObject.has("value") ? parseIntValue(fieldOJsonObject, "value"):null;
        Integer multiplier = fieldOJsonObject.has("multiplier") ? fieldOJsonObject.getInt("multiplier"):null;
        return new XmlvalueAndMultiplierSettings(multiplier, value);
    }

    public XmlDisplacementAndMultiplierSettings derSettingsDisplacementExtractor(JSONObject derPayload, String rtg)
    {
        if (!derPayload.has(rtg)) {
            return null;
        }
        JSONObject fieldObject = derPayload.getJSONObject(rtg);
        Integer displacement = fieldObject.has("displacement") ? fieldObject.getInt("displacement") : null;
        Integer multiplier = fieldObject.has("multiplier") ? fieldObject.getInt("multiplier") : null;
        return new XmlDisplacementAndMultiplierSettings(displacement, multiplier);
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

   

    public String getDerCapabilityHttp(Long endDeviceId, Long derId) {
        try {
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId, derId);
            if (derEntityOptional.isEmpty()) {
                return "<DERCapability xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dercap\">\n" +
                       "  <message>No DERCapability found for EndDevice " + endDeviceId + " and DER ID " + derId + "</message>\n" +
                       "</DERCapability>";
            }
    
            DerEntity derEntity = derEntityOptional.get();
            DERCapabilityEntity derCap = derEntity.getDerCapability();
            if (derCap == null) {
                return "<DERCapability xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dercap\">\n" +
                       "  <message>No DERCapability entity found for this DER</message>\n" +
                       "</DERCapability>";
            }
    
            StringBuilder xml = new StringBuilder();
            xml.append("<DERCapability xmlns=\"urn:ieee:std:2030.5:ns\" ")
               .append("subscribable=\"1\" ")  // DER Capability is always subscribable for device updates
               .append("href=\"").append(stripHost(derEntity.getDerCapabilityLink())).append("\">\n");
    
            // Simple values
            appendSimpleElement(xml, "modesSupported", derCap.getModesSupported());
            appendSimpleElement(xml, "rtgAbnormalCategory", derCap.getRtgAbnormalCategory());
    
            // Value + Multiplier fields (IEEE 2030.5 physical values)
            appendPhysicalValue(xml, "rtgMaxA", derCap.getRtgMaxAMultiplier(), derCap.getRtgMaxAValue());
            appendPhysicalValue(xml, "rtgMaxAh", derCap.getRtgMaxAhMultiplier(), derCap.getRtgMaxAhValue());
            appendPhysicalValue(xml, "rtgMaxChargeRateVA", derCap.getRtgMaxChargeRateVAMultiplier(), derCap.getRtgMaxChargeRateVAValue());
            appendPhysicalValue(xml, "rtgMaxChargeRateW", derCap.getRtgMaxChargeRateWMultiplier(), derCap.getRtgMaxChargeRateWValue());
            appendPhysicalValue(xml, "rtgMaxDischargeRateVA", derCap.getRtgMaxDischargeRateVAMultiplier(), derCap.getRtgMaxDischargeRateVAValue());
            appendPhysicalValue(xml, "rtgMaxDischargeRateW", derCap.getRtgMaxDischargeRateWMultiplier(), derCap.getRtgMaxDischargeRateWValue());
            appendPhysicalValue(xml, "rtgMaxV", derCap.getRtgMaxVMultiplier(), derCap.getRtgMaxVValue());
            appendPhysicalValue(xml, "rtgMaxVA", derCap.getRtgMaxVAMultiplier(), derCap.getRtgMaxVAValue());
            appendPhysicalValue(xml, "rtgMaxVar", derCap.getRtgMaxVarMultiplier(), derCap.getRtgMaxVarValue());
            appendPhysicalValue(xml, "rtgMaxVarNeg", derCap.getRtgMaxVarNegMultiplier(), derCap.getRtgMaxVarNegValue());
            appendPhysicalValue(xml, "rtgMaxW", derCap.getRtgMaxWMultiplier(), derCap.getRtgMaxWValue());
            appendPhysicalValue(xml, "rtgMaxWh", derCap.getRtgMaxWhMultiplier(), derCap.getRtgMaxWhValue());
    
            // Displacement + Multiplier fields (Power Factor)
            appendPowerFactorValue(xml, "rtgMinPFOverExcited", derCap.getRtgMinPFOverExcitedDisplacement(), derCap.getRtgMinPFOverExcitedMultiplier());
            appendPowerFactorValue(xml, "rtgMinPFUnderExcited", derCap.getRtgMinPFUnderExcitedDisplacement(), derCap.getRtgMinPFUnderExcitedMultiplier());
    
            // More physical values
            appendPhysicalValue(xml, "rtgMinV", derCap.getRtgMinVMultiplier(), derCap.getRtgMinVValue());
    
            // Simple value
            appendSimpleElement(xml, "rtgNormalCategory", derCap.getRtgNormalCategory());
    
            // More displacement + multiplier
            appendPowerFactorValue(xml, "rtgOverExcitedPF", derCap.getRtgOverExcitedPFDisplacement(), derCap.getRtgOverExcitedPFMultiplier());
    
            // More physical values
            appendPhysicalValue(xml, "rtgOverExcitedW", derCap.getRtgOverExcitedWMultiplier(), derCap.getRtgOverExcitedWValue());
            appendPhysicalValue(xml, "rtgReactiveSusceptance", derCap.getRtgReactiveSusceptanceMultiplier(), derCap.getRtgReactiveSusceptanceValue());
    
            // More displacement + multiplier
            appendPowerFactorValue(xml, "rtgUnderExcitedPF", derCap.getRtgUnderExcitedPFDisplacement(), derCap.getRtgUnderExcitedPFMultiplier());
    
            // More physical values
            appendPhysicalValue(xml, "rtgUnderExcitedW", derCap.getRtgUnderExcitedWMultiplier(), derCap.getRtgUnderExcitedWValue());
            appendPhysicalValue(xml, "rtgVNom", derCap.getRtgVNomMultiplier(), derCap.getRtgVNomValue());
    
            // Type
            appendSimpleElement(xml, "type", derCap.getDerType());
    
            xml.append("</DERCapability>");
            return xml.toString();
    
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERCapability", e);
            return "<DERCapability xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dercap\">\n" +
                   "  <error>Some error occurred</error>\n" +
                   "</DERCapability>";
        }
    }
    
    // Helper method for simple elements (String, Integer, Long)
    private void appendSimpleElement(StringBuilder xml, String name, Object value) {
        if (value != null) {
            xml.append("  <").append(name).append(">").append(value).append("</").append(name).append(">\n");
        }
    }
    
    // Helper method for physical values (multiplier + value) - always include even if null
    private void appendPhysicalValue(StringBuilder xml, String name, Integer multiplier, Integer value) {
        xml.append("  <").append(name).append(">\n");
        xml.append("    <multiplier>").append(multiplier != null ? multiplier : 0).append("</multiplier>\n");
        xml.append("    <value>").append(value != null ? value : 0).append("</value>\n");
        xml.append("  </").append(name).append(">\n");
    }
    
    // Helper method for power factor values (displacement + multiplier)
    private void appendPowerFactorValue(StringBuilder xml, String name, Integer displacement, Integer multiplier) {
        if (displacement != null || multiplier != null) {
            xml.append("  <").append(name).append(">\n");
            xml.append("    <displacement>").append(displacement != null ? displacement : 0).append("</displacement>\n");
            xml.append("    <multiplier>").append(multiplier != null ? multiplier : 0).append("</multiplier>\n");
            xml.append("  </").append(name).append(">\n");
        }
    }



    public String getDerSettingsHttp(Long endDeviceId, Long derId) {
        try {
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId, derId);
            if (derEntityOptional.isEmpty()) {
                return "<DERSettings xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/ders\">\n" +
                       "  <message>No DER found for EndDevice " + endDeviceId + " and DER ID " + derId + "</message>\n" +
                       "</DERSettings>";
            }
    
            DerEntity derEntity = derEntityOptional.get();
            DERSettingsEntity derSettings = derEntity.getDerSettings();
            if (derSettings == null) {
                return "<DERSettings xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/ders\">\n" +
                       "  <message>No DERSettings entity found for this DER</message>\n" +
                       "</DERSettings>";
            }
    
            StringBuilder xml = new StringBuilder();
            xml.append("<DERSettings xmlns=\"urn:ieee:std:2030.5:ns\" ")
               .append("subscribable=\"1\" ")  // DER Settings is always subscribable for device updates
               .append("href=\"").append(stripHost(derEntity.getDerSettingsLink())).append("\">\n");
    
            // Simple values
            appendSimpleElement(xml, "modesEnabled", derSettings.getModesEnabled());
            appendSimpleElement(xml, "setESDelay", derSettings.getSetESDelay());
            appendSimpleElement(xml, "setESHighFreq", derSettings.getSetESHighFreq());
            appendSimpleElement(xml, "setESHighVolt", derSettings.getSetESHighVolt());
            appendSimpleElement(xml, "setESLowFreq", derSettings.getSetESLowFreq());
            appendSimpleElement(xml, "setESLowVolt", derSettings.getSetESLowVolt());
            appendSimpleElement(xml, "setESRampTms", derSettings.getSetESRampTms());
            appendSimpleElement(xml, "setESRandomDelay", derSettings.getSetESRandomDelay());
            appendSimpleElement(xml, "setGradW", derSettings.getSetGradW());
    
            // Physical values (multiplier + value)
            appendPhysicalValue(xml, "setMaxDischargeRateW", derSettings.getSetMaxDischargeRateWMultiplier(), derSettings.getSetMaxDischargeRateWValue());
            appendPhysicalValue(xml, "setMaxV", derSettings.getSetMaxVMultiplier(), derSettings.getSetMaxVValue());
            appendPhysicalValue(xml, "setMaxVA", derSettings.getSetMaxVAMultiplier(), derSettings.getSetMaxVAValue());
            appendPhysicalValue(xml, "setMaxVar", derSettings.getSetMaxVarMultiplier(), derSettings.getSetMaxVarValue());
            appendPhysicalValue(xml, "setMaxVarNeg", derSettings.getSetMaxVarNegMultiplier(), derSettings.getSetMaxVarNegValue());
            appendPhysicalValue(xml, "setMaxW", derSettings.getSetMaxWMultiplier(), derSettings.getSetMaxWValue());
    
            // Displacement + Multiplier fields (Power Factor)
            appendPowerFactorValue(xml, "setMinPFOverExcited", derSettings.getSetMinPFOverExcitedDisplacement(), derSettings.getSetMinPFOverExcitedMultiplier());
            appendPowerFactorValue(xml, "setMinPFUnderExcited", derSettings.getSetMinPFUnderExcitedDisplacement(), derSettings.getSetMinPFUnderExcitedMultiplier());
    
            // More physical values
            appendPhysicalValue(xml, "setMinV", derSettings.getSetMinVMultiplier(), derSettings.getSetMinVValue());
    
            // More simple values
            appendSimpleElement(xml, "setSoftGradW", derSettings.getSetSoftGradW());
    
            // More physical values
            appendPhysicalValue(xml, "setVNom", derSettings.getSetVNomMultiplier(), derSettings.getSetVNomValue());
            appendPhysicalValue(xml, "setVRef", derSettings.getSetVRefMultiplier(), derSettings.getSetVRefValue());
    
            // Updated time
            appendSimpleElement(xml, "updatedTime", derSettings.getUpdatedTime());
    
            xml.append("</DERSettings>");
            return xml.toString();
    
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERSettings", e);
            return "<DERSettings xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/ders\">\n" +
                   "  <error>Some error occurred</error>\n" +
                   "</DERSettings>";
        }
    }

    public String getDerStatusHttp(Long endDeviceId, Long derId) {
        try {
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId, derId);
            if (derEntityOptional.isEmpty()) {
                return "<DERStatus xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dstat\">\n" +
                       "  <message>No DER found for EndDevice " + endDeviceId + " and DER ID " + derId + "</message>\n" +
                       "</DERStatus>";
            }
    
            DerEntity derEntity = derEntityOptional.get();
            DERStatusEntity derStatus = derEntity.getDerStatus();
            if (derStatus == null) {
                return "<DERStatus xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dstat\">\n" +
                       "  <message>No DERStatus entity found for this DER</message>\n" +
                       "</DERStatus>";
            }
    
            StringBuilder xml = new StringBuilder();
            xml.append("<DERStatus xmlns=\"urn:ieee:std:2030.5:ns\">\n");
    
            // Simple values
            appendSimpleElement(xml, "alarmStatus", derStatus.getAlarmStatus());
            appendSimpleElement(xml, "readingTime", derStatus.getReadingTime());
            appendSimpleElement(xml, "manufacturerStatus", derStatus.getManufacturerStatus());
    
            // Status fields with dateTime and value
            appendStatusValue(xml, "genConnectStatus", derStatus.getGenConnectStatusDateTime(), derStatus.getGenConnectStatusValue());
            appendStatusValue(xml, "inverterStatus", derStatus.getInverterStatusDateTime(), derStatus.getInverterStatusValue());
            appendStatusValue(xml, "localControlModeStatus", derStatus.getLocalControlModeStatusDateTime(), derStatus.getLocalControlModeStatusValue());
            appendStatusValue(xml, "operationalModeStatus", derStatus.getOperationalModeStatusDateTime(), derStatus.getOperationalModeStatusValue());
            appendStatusValue(xml, "stateOfChargeStatus", derStatus.getStateOfChargeStatusDateTime(), derStatus.getStateOfChargeStatusValue());
            appendStatusValue(xml, "storageModeStatus", derStatus.getStorageModeStatusDateTime(), derStatus.getStorageModeStatusValue());
            appendStatusValue(xml, "storConnectStatus", derStatus.getStorConnectStatusDateTime(), derStatus.getStorConnectStatusValue());
    
            xml.append("</DERStatus>");
            return xml.toString();
    
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERStatus", e);
            return "<DERStatus xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dstat\">\n" +
                   "  <error>Some error occurred</error>\n" +
                   "</DERStatus>";
        }
    }

    // Helper method for status values (dateTime + value) - always include even if null
    private void appendStatusValue(StringBuilder xml, String name, Long dateTime, Integer value) {
        xml.append("  <").append(name).append(">\n");
        xml.append("    <dateTime>").append(dateTime != null ? dateTime : 0).append("</dateTime>\n");
        xml.append("    <value>").append(value != null ? value : 0).append("</value>\n");
        xml.append("  </").append(name).append(">\n");
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
                return "<DER xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "\">\n" +
                    " <message>No DER found for EndDevice " + endDeviceId + " and DER ID " + derId + "</message>\n" +
                    "</DER>";
            }

            DerEntity derEntity = derEntityOptional.get();
            StringBuilder xml = new StringBuilder();
            xml.append("<DER xmlns=\"urn:ieee:std:2030.5:ns\" ")
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
            return "<DER xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "\">\n" +
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

    

    private Integer parseIntValue(org.json.JSONObject obj, String key) {
        if (!obj.has(key)) return null;
        String s = obj.optString(key, "0").replaceAll("[^0-9-]", "");
        return s.isEmpty() ? 0 : Integer.parseInt(s);
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
        xml.append("<DERList xmlns=\"urn:ieee:std:2030.5:ns\" ")
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
    public String updateDERSettings(Long endDeviceID, Long derId, JSONObject payload)
    throws  NumberFormatException, JSONException, NotFoundException {
        LOGGER.info("Received DER Settings update payload: " + payload);

        EndDeviceEntity endDevice = endDeviceRepository.findById(endDeviceID)
        .orElseThrow(() -> new NotFoundException());
        LOGGER.info("DER service Settings update for endDeviceId: " + endDeviceID + " and derID: " + derId);

        DerEntity derEntity = derRepository.findById(derId)
                .orElseThrow(() -> new NotFoundException());

        derEntity.setEndDevice(endDevice);

        
        try {
            derEntity = derRepository.save(derEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER entity", e);
        }

        // Create DERSettingsEntity if it doesn't exist
        if (derEntity.getDerSettings() == null) {
            DERSettingsEntity newSettings = new DERSettingsEntity();
            derEntity.setDerSettings(newSettings);
            derEntity = derRepository.save(derEntity);
        }

        updateDerSettingsFields(derEntity.getDerSettings(), payload);
        derSettingsRepository.save(derEntity.getDerSettings());

        try {
            LOGGER.info("DER Settings updated successfully");
            return getDerCapabilityHttp(endDeviceID, derId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DER entity", e);
            return "Error retrieving DER entity";
        }
    }




     @Transactional
     public String updateDERCapability(Long endDeviceID, Long derId, JSONObject payload)
             throws NumberFormatException, JSONException, NotFoundException {
        LOGGER.info("Received DER Capability update payload: " + payload);

        EndDeviceEntity endDevice = endDeviceRepository.findById(endDeviceID)
                .orElseThrow(() -> new NotFoundException());

        LOGGER.info("DER service Capability update for endDeviceId: " + endDeviceID + " and derID: " + derId);

        DerEntity derEntity = derRepository.findById(derId)
                .orElseThrow(() -> new NotFoundException());

        derEntity.setEndDevice(endDevice);

        try {
            derEntity = derRepository.save(derEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER entity", e);
        }

        // Create DERCapabilityEntity if it doesn't exist
        if (derEntity.getDerCapability() == null) {
            DERCapabilityEntity newCapability = new DERCapabilityEntity();
            derEntity.setDerCapability(newCapability);
            derEntity = derRepository.save(derEntity);
        }

        updateDerCapabilityFields(derEntity.getDerCapability(), payload);
        derCapabilityRepository.save(derEntity.getDerCapability());

        try {
            LOGGER.info("DER Capability updated successfully");
            return getDerCapabilityHttp(endDeviceID, derId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DER entity", e);
            return "Error retrieving DER entity";
        }
     }

    
     public void updateDerSettingsFields(DERSettingsEntity derSettings, JSONObject payload) {
        // Simple values
        derSettings.setModesEnabled(payload.has("modesEnabled") ? payload.getString("modesEnabled") : null);
        derSettings.setSetESDelay(payload.has("setESDelay") ? payload.getLong("setESDelay") : null);
        derSettings.setSetESHighFreq(payload.has("setESHighFreq") ? payload.getLong("setESHighFreq") : null);
        derSettings.setSetESHighVolt(payload.has("setESHighVolt") ? payload.getLong("setESHighVolt") : null);
        derSettings.setSetESLowFreq(payload.has("setESLowFreq") ? payload.getLong("setESLowFreq") : null);
        derSettings.setSetESLowVolt(payload.has("setESLowVolt") ? payload.getLong("setESLowVolt") : null);
        derSettings.setSetESRampTms(payload.has("setESRampTms") ? payload.getLong("setESRampTms") : null);
        derSettings.setSetESRandomDelay(payload.has("setESRandomDelay") ? payload.getLong("setESRandomDelay") : null);
        derSettings.setSetGradW(payload.has("setGradW") ? payload.getLong("setGradW") : null);
        derSettings.setSetSoftGradW(payload.has("setSoftGradW") ? payload.getLong("setSoftGradW") : null);
        derSettings.setUpdatedTime(payload.has("updatedTime") ? payload.getLong("updatedTime") : null);
    
        // Value + Multiplier fields
        derSettings.setSetMaxDischargeRateW(derSettingsExtractor(payload, "setMaxDischargeRateW"));
        derSettings.setSetMaxV(derSettingsExtractor(payload, "setMaxV"));
        derSettings.setSetMaxVA(derSettingsExtractor(payload, "setMaxVA"));
        derSettings.setSetMaxVar(derSettingsExtractor(payload, "setMaxVar"));
        derSettings.setSetMaxVarNeg(derSettingsExtractor(payload, "setMaxVarNeg"));
        derSettings.setSetMaxW(derSettingsExtractor(payload, "setMaxW"));
        derSettings.setSetMinV(derSettingsExtractor(payload, "setMinV"));
        derSettings.setSetVNom(derSettingsExtractor(payload, "setVNom"));
        derSettings.setSetVRef(derSettingsExtractor(payload, "setVRef"));
    
        // Displacement + Multiplier fields
        derSettings.setSetMinPFOverExcited(derSettingsDisplacementExtractor(payload, "setMinPFOverExcited"));
        derSettings.setSetMinPFUnderExcited(derSettingsDisplacementExtractor(payload, "setMinPFUnderExcited"));
    }





    private void updateDerCapabilityFields(DERCapabilityEntity derCapability, JSONObject payload) {
        // Simple values
        derCapability.setModesSupported(payload.has("modesSupported") ? payload.getString("modesSupported") : null);
        derCapability.setRtgAbnormalCategory(payload.has("rtgAbnormalCategory") ? payload.getInt("rtgAbnormalCategory") : null);
        derCapability.setRtgNormalCategory(payload.has("rtgNormalCategory") ? payload.getInt("rtgNormalCategory") : null);
        derCapability.setDerType(payload.has("type") ? payload.getInt("type") : null);

        // Value + Multiplier fields
        derCapability.setRtgMaxA(derCapablityExtractor(payload, "rtgMaxA"));
        derCapability.setRtgMaxAh(derCapablityExtractor(payload, "rtgMaxAh"));
        derCapability.setRtgMaxChargeRateVA(derCapablityExtractor(payload, "rtgMaxChargeRateVA"));
        derCapability.setRtgMaxChargeRateW(derCapablityExtractor(payload, "rtgMaxChargeRateW"));
        derCapability.setRtgMaxDischargeRateW(derCapablityExtractor(payload, "rtgMaxDischargeRateW"));
        derCapability.setRtgMaxDischargeRateVA(derCapablityExtractor(payload, "rtgMaxDischargeRateVA"));
        derCapability.setRtgMaxV(derCapablityExtractor(payload, "rtgMaxV"));
        derCapability.setRtgMaxVA(derCapablityExtractor(payload, "rtgMaxVA"));
        derCapability.setRtgMaxVar(derCapablityExtractor(payload, "rtgMaxVar"));
        derCapability.setRtgMaxVarNeg(derCapablityExtractor(payload, "rtgMaxVarNeg"));
        derCapability.setRtgMaxW(derCapablityExtractor(payload, "rtgMaxW"));
        derCapability.setRtgMaxWh(derCapablityExtractor(payload, "rtgMaxWh"));
        derCapability.setRtgMinV(derCapablityExtractor(payload, "rtgMinV"));
        derCapability.setRtgOverExcitedW(derCapablityExtractor(payload, "rtgOverExcitedW"));
        derCapability.setRtgReactiveSusceptance(derCapablityExtractor(payload, "rtgReactiveSusceptance"));
        derCapability.setRtgUnderExcitedW(derCapablityExtractor(payload, "rtgUnderExcitedW"));
        derCapability.setRtgVNom(derCapablityExtractor(payload, "rtgVNom"));

        // Displacement + Multiplier fields
        derCapability.setRtgMinPFOverExcited(derDisplacementExtractor(payload, "rtgMinPFOverExcited"));
        derCapability.setRtgMinPFUnderExcited(derDisplacementExtractor(payload, "rtgMinPFUnderExcited"));
        derCapability.setRtgOverExcitedPF(derDisplacementExtractor(payload, "rtgOverExcitedPF"));
        derCapability.setRtgUnderExcitedPF(derDisplacementExtractor(payload, "rtgUnderExcitedPF"));
    }

    @Transactional
    public String updateDERStatus(Long endDeviceID, Long derId, JSONObject payload) 
    throws NumberFormatException, JSONException, NotFoundException
    {
        LOGGER.info("Received DER Status update payload: " + payload);

        EndDeviceEntity endDevice = endDeviceRepository.findById(endDeviceID)
        .orElseThrow(() -> new NotFoundException());
        
        DerEntity derEntity = derRepository.findById(derId)
                .orElseThrow(() -> new NotFoundException());

        derEntity.setEndDevice(endDevice);
        try {
            derEntity = derRepository.save(derEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER entity", e);
        }

        // Create DERStatus if it doesn't exist
        if (derEntity.getDerStatus() == null) {
            DERStatusEntity newStatus = new DERStatusEntity();
            derEntity.setDerStatus(newStatus);
            derEntity = derRepository.save(derEntity);
        }

        updateDERStatusFields(derEntity.getDerStatus(), payload);
        derStatusRepository.save(derEntity.getDerStatus());

        LOGGER.info("DER Status updated successfully");
        return getDerStatusHttp(endDeviceID, derId);
    }

    
     private void updateDERStatusFields(DERStatusEntity derStatusEntity, JSONObject payload)
     {
   
        derStatusEntity.setAlarmStatus(payload.has("alarmStatus") ? payload.getString("alarmStatus") : null);
        derStatusEntity.setReadingTime(payload.has("readingTime") ? payload.getLong("readingTime") : null);
        derStatusEntity.setManufacturerStatus(payload.has("manufacturerStatus") ? payload.getString("manufacturerStatus") : null);

        
        if (payload.has("genConnectStatus")) {
            JSONObject genConnect = payload.getJSONObject("genConnectStatus");
            derStatusEntity.setGenConnectStatusDateTime(genConnect.has("dateTime") ? genConnect.getLong("dateTime") : null);
            derStatusEntity.setGenConnectStatusValue(genConnect.has("value") ? parseIntValue(genConnect, "value") : null);
        }

        
        if (payload.has("inverterStatus")) {
            JSONObject inverter = payload.getJSONObject("inverterStatus");
            derStatusEntity.setInverterStatusDateTime(inverter.has("dateTime") ? inverter.getLong("dateTime") : null);
            derStatusEntity.setInverterStatusValue(inverter.has("value") ? parseIntValue(inverter, "value") : null);
        }

       
        if (payload.has("localControlModeStatus")) {
            JSONObject localControl = payload.getJSONObject("localControlModeStatus");
            derStatusEntity.setLocalControlModeStatusDateTime(localControl.has("dateTime") ? localControl.getLong("dateTime") : null);
            derStatusEntity.setLocalControlModeStatusValue(localControl.has("value") ? parseIntValue(localControl, "value") : null);
        }

        
        if (payload.has("operationalModeStatus")) {
            JSONObject operational = payload.getJSONObject("operationalModeStatus");
            derStatusEntity.setOperationalModeStatusDateTime(operational.has("dateTime") ? operational.getLong("dateTime") : null);
            derStatusEntity.setOperationalModeStatusValue(operational.has("value") ? parseIntValue(operational, "value") : null);
        }

        
        if (payload.has("stateOfChargeStatus")) {
            JSONObject stateOfCharge = payload.getJSONObject("stateOfChargeStatus");
            derStatusEntity.setStateOfChargeStatusDateTime(stateOfCharge.has("dateTime") ? stateOfCharge.getLong("dateTime") : null);
            derStatusEntity.setStateOfChargeStatusValue(stateOfCharge.has("value") ? parseIntValue(stateOfCharge, "value") : null);
        }

     
        if (payload.has("storageModeStatus")) {
            JSONObject storageMode = payload.getJSONObject("storageModeStatus");
            derStatusEntity.setStorageModeStatusDateTime(storageMode.has("dateTime") ? storageMode.getLong("dateTime") : null);
            derStatusEntity.setStorageModeStatusValue(storageMode.has("value") ? parseIntValue(storageMode, "value") : null);
        }

        
        if (payload.has("storConnectStatus")) {
            JSONObject storConnect = payload.getJSONObject("storConnectStatus");
            derStatusEntity.setStorConnectStatusDateTime(storConnect.has("dateTime") ? storConnect.getLong("dateTime") : null);
            derStatusEntity.setStorConnectStatusValue(storConnect.has("value") ? parseIntValue(storConnect, "value") : null);
        }
     }

    @Transactional
    public String updateDERAvailability(Long endDeviceID, Long derId, JSONObject payload)
    throws NumberFormatException, JSONException, NotFoundException {
        LOGGER.info("Received DER Availability update payload: " + payload);

        EndDeviceEntity endDevice = endDeviceRepository.findById(endDeviceID)
        .orElseThrow(() -> new NotFoundException());
        
        DerEntity derEntity = derRepository.findById(derId)
                .orElseThrow(() -> new NotFoundException());

        derEntity.setEndDevice(endDevice);
        try {
            derEntity = derRepository.save(derEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving DER entity", e);
        }

        // Create DERAvailability if it doesn't exist
        if (derEntity.getDerAvailability() == null) {
            DERAvailabilityEntity newAvail = new DERAvailabilityEntity();
            derEntity.setDerAvailability(newAvail);
            derEntity = derRepository.save(derEntity);
        }

        updateDERAvailabilityFields(derEntity.getDerAvailability(), payload);
        derAvailabilityRepository.save(derEntity.getDerAvailability());

        LOGGER.info("DER Availability updated successfully");
        return "DER Availability updated";
    }

    private void updateDERAvailabilityFields(DERAvailabilityEntity derAvail, JSONObject payload) {
        derAvail.setAvailabilityDuration(payload.has("availabilityDuration") ? payload.getLong("availabilityDuration") : null);
        derAvail.setMaxChargeDuration(payload.has("maxChargeDuration") ? payload.getLong("maxChargeDuration") : null);
        derAvail.setReadingTime(payload.has("readingTime") ? payload.getLong("readingTime") : null);
        derAvail.setReserveChargePercent(payload.has("reserveChargePercent") ? payload.getInt("reserveChargePercent") : null);
        derAvail.setReservePercent(payload.has("reservePercent") ? payload.getInt("reservePercent") : null);

        // statVarAvail (multiplier + value)
        if (payload.has("statVarAvail")) {
            JSONObject statVar = payload.getJSONObject("statVarAvail");
            derAvail.setStatVarAvailMultiplier(statVar.has("multiplier") ? statVar.getInt("multiplier") : null);
            derAvail.setStatVarAvailValue(statVar.has("value") ? parseIntValue(statVar, "value") : null);
        }

        // statWAvail (multiplier + value)
        if (payload.has("statWAvail")) {
            JSONObject statW = payload.getJSONObject("statWAvail");
            derAvail.setStatWAvailMultiplier(statW.has("multiplier") ? statW.getInt("multiplier") : null);
            derAvail.setStatWAvailValue(statW.has("value") ? parseIntValue(statW, "value") : null);
        }
    }

    public String getDerAvailabilityHttp(Long endDeviceId, Long derId) {
        try {
            Optional<DerEntity> derEntityOptional = derRepository.findFirstByEndDeviceIdAndId(endDeviceId, derId);
            if (derEntityOptional.isEmpty()) {
                return "<DERAvailability xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dera\">\n" +
                       "  <message>No DER found for EndDevice " + endDeviceId + " and DER ID " + derId + "</message>\n" +
                       "</DERAvailability>";
            }
    
            DerEntity derEntity = derEntityOptional.get();
            DERAvailabilityEntity derAvail = derEntity.getDerAvailability();
            if (derAvail == null) {
                return "<DERAvailability xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dera\">\n" +
                       "  <message>No DERAvailability entity found for this DER</message>\n" +
                       "</DERAvailability>";
            }
    
            StringBuilder xml = new StringBuilder();
            xml.append("<DERAvailability xmlns=\"urn:ieee:std:2030.5:ns\">\n");
    
            appendSimpleElement(xml, "availabilityDuration", derAvail.getAvailabilityDuration());
            appendSimpleElement(xml, "maxChargeDuration", derAvail.getMaxChargeDuration());
            appendSimpleElement(xml, "readingTime", derAvail.getReadingTime());
            appendSimpleElement(xml, "reserveChargePercent", derAvail.getReserveChargePercent());
            appendSimpleElement(xml, "reservePercent", derAvail.getReservePercent());
            appendPhysicalValue(xml, "statVarAvail", derAvail.getStatVarAvailMultiplier(), derAvail.getStatVarAvailValue());
            appendPhysicalValue(xml, "statWAvail", derAvail.getStatWAvailMultiplier(), derAvail.getStatWAvailValue());
    
            xml.append("</DERAvailability>");
            return xml.toString();
    
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERAvailability", e);
            return "<DERAvailability xmlns=\"urn:ieee:std:2030.5:ns\" href=\"/edev/" + endDeviceId + "/der/" + derId + "/dera\">\n" +
                   "  <error>Some error occurred</error>\n" +
                   "</DERAvailability>";
        }
    }

    @Transactional
     public ResponseEntity<Map<String, Object>> updatePowerGenerationTest(Long endDeviceID, Long derId,JSONObject payload)throws NumberFormatException, JSONException, NotFoundException 
     {
        LOGGER.info("Received DER power generation payload is " + payload); 
        Long endDeviceId = payload.getLong("endDeviceId"); 
        EndDeviceEntity endDevice = endDeviceRepository.findById( endDeviceId)
        .orElseThrow(() -> new NotFoundException());
        Long derID = payload.getLong("derID"); 
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

            // Create nested map for setMaxW
            Map<String, Integer> setMaxWMap = new HashMap<>();
            setMaxWMap.put("value", derEntity.getDerSettings().getSetMaxWValue());
            setMaxWMap.put("multiplier", derEntity.getDerSettings().getSetMaxWMultiplier());
            entityMap.put("setMaxW", setMaxWMap);

            // Create nested map for setMaxVA
            Map<String, Integer> setMaxVAMap = new HashMap<>();
            setMaxVAMap.put("value", derEntity.getDerSettings().getSetMaxVAValue());
            setMaxVAMap.put("multiplier", derEntity.getDerSettings().getSetMaxVAMultiplier());
            entityMap.put("setMaxVA", setMaxVAMap);

            LOGGER.info("DER service Power generation test - setMaxW: " + setMaxWMap + ", setMaxVA: " + setMaxVAMap);
            result.put("DerPowerGenerationTest", entityMap);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DER entity", e);
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));

     }
    }

     public void setPowerGeneration(DerEntity derEntity, JSONObject derPowerGenerationpayload)
     {
        // Create DERSettingsEntity if it doesn't exist
        if (derEntity.getDerSettings() == null) {
            DERSettingsEntity newSettings = new DERSettingsEntity();
            derEntity.setDerSettings(newSettings);
        }

        XmlvalueAndMultiplierSettings setMaxW = derSettingsExtractor(derPowerGenerationpayload, "setMaxW");
        XmlvalueAndMultiplierSettings setMaxVA = derSettingsExtractor(derPowerGenerationpayload, "setMaxVA");
        derEntity.getDerSettings().setSetMaxW(setMaxW);
        derEntity.getDerSettings().setSetMaxVA(setMaxVA);
     }








    
}



