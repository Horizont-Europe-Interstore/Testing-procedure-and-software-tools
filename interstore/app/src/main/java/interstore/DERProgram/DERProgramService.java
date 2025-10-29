package interstore.DERProgram;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsEntity;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsRepository;
import interstore.Identity.*;
import interstore.Types.HexBinary128;
import interstore.Types.mRIDType;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class DERProgramService {
    private static final Logger LOGGER = Logger.getLogger(DERProgramService.class.getName());

    @Autowired
    DERProgramRepository derProgramRepository;
    
    @Autowired
    SubscribableIdentifiedObjectRepository subscribableIdentifiedObjectRepository;
    
    @Autowired
    SubscribableResourceRepository subscribableResourceRepository;

    @Autowired
    FunctionSetAssignmentsRepository functionSetAssignmentsRepository;
        
    @Transactional
    public DERProgramEntity createDerProgram(JSONObject payload) throws NumberFormatException, JSONException, NotFoundException {
        Long fsaId = Long.parseLong(payload.getJSONObject("payload").getString("fsaID"));
        FunctionSetAssignmentsEntity fsaEntity = functionSetAssignmentsRepository.findFsaById(fsaId);
        DERProgramEntity derProgram = new DERProgramEntity();
        derProgram.setFunctionSetAssignmentEntity(fsaEntity);
        SubscribableIdentifiedObjectEntity subscribableIdentifiedObjectEntity = new SubscribableIdentifiedObjectEntity();
        subscribableIdentifiedObjectEntity.setFunctionSetAssignmentEntity(fsaEntity);
        SubscribableResourceEntity subscribableResourceEntity = new SubscribableResourceEntity();
    
        try {
            derProgramRepository.save(derProgram);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving der program entity", e);
        }
    
        try {
            subscribableIdentifiedObjectRepository.save(subscribableIdentifiedObjectEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving subscribableIdentifiedObjectEntity entity", e);
        }
    
        try {
            subscribableResourceRepository.save(subscribableResourceEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving subscribableResourceEntity entity", e);
        }
    
        derProgram = setDerProgramEntity(payload, derProgram, subscribableIdentifiedObjectEntity, subscribableResourceEntity, fsaEntity);
        return derProgram;
    }
    
    public DERProgramEntity setDerProgramEntity(JSONObject payload, DERProgramEntity derProgram,
                                                SubscribableIdentifiedObjectEntity subscribableIdentifiedObjectEntity,
                                                SubscribableResourceEntity subscribableResourceEntity,
                                                FunctionSetAssignmentsEntity fsaEntity) {
        JSONObject DerProgrampayload = payload.optJSONObject("payload");
    
        short subscribable = (short) DerProgrampayload.optInt("subscribable");
        String mRID = DerProgrampayload.optString("mRID");
        String mRIDString = HexBinary128.validateAndFormatHexValue(mRID);
        mRIDType mRIDValue = new mRIDType(mRIDString);
        String description = DerProgrampayload.optString("description");
        String version = DerProgrampayload.optString("version");
        Integer versionInt = version.isEmpty() ? null : Integer.parseInt(version);
        String primacy = DerProgrampayload.optString("primacy");
        Short primacyShort = primacy.isEmpty() ? null : Short.parseShort(primacy);
        String activeDERControlListLink = DerProgrampayload.optString("activeDERControlListLink", null);  
        String defaultDERControlLink = DerProgrampayload.optString("defaultDERControlLink", null);        
        String dERControlListLink = DerProgrampayload.optString("dERControlListLink", null);              
        String dERCurveListLink = DerProgrampayload.optString("dERCurveListLink", null);                  
        subscribableIdentifiedObjectEntity.setFunctionSetAssignmentEntity(fsaEntity);
        subscribableIdentifiedObjectEntity = setSubscribableIdentifiedObject(subscribableIdentifiedObjectEntity, mRIDValue.toString(), description, versionInt);
        subscribableIdentifiedObjectRepository.save(subscribableIdentifiedObjectEntity);
    
        subscribableResourceEntity.setSubscribable(subscribable);
        subscribableResourceRepository.save(subscribableResourceEntity);
    
        String derListLink = fsaEntity.getDERProgramListLink();
        LOGGER.log(Level.INFO, "DER Program List Link: " + derListLink);
        LOGGER.log(Level.INFO, "Function Set Assignments Entity: " + fsaEntity);
    
        derProgram.setFunctionSetAssignmentEntity(fsaEntity);

        Long derId = derProgram.getId();
        derProgram.setPrimacy(primacyShort);
    
        String idString = derListLink + "/" + String.valueOf(derId) + "/";
    
        derProgram.setActiveDERControlListLink(activeDERControlListLink != null && activeDERControlListLink != ""? idString + activeDERControlListLink : null);
        derProgram.setDefaultDERControlLink(defaultDERControlLink != null && defaultDERControlLink != ""? idString + defaultDERControlLink : null);
        derProgram.setDERControlListLink(dERControlListLink != null && dERControlListLink != ""? idString + dERControlListLink : null);
        derProgram.setDERCurveListLink(dERCurveListLink != null && dERCurveListLink != "" ? idString + dERCurveListLink : null);
    
        derProgram.setSubscribableIdentifiedObject(subscribableIdentifiedObjectEntity);
        derProgram.setSubscribableResource(subscribableResourceEntity);
    
        derProgramRepository.save(derProgram);
    
        return derProgram;
    }
    

     public SubscribableIdentifiedObjectEntity  setSubscribableIdentifiedObject (SubscribableIdentifiedObjectEntity subscribableIdentifiedObjectEntity,
     String mRID, String description , Integer versionInt ){
        subscribableIdentifiedObjectEntity.setmRID(mRID); 
        subscribableIdentifiedObjectEntity.setDescription(description);
        subscribableIdentifiedObjectEntity.setVersion(versionInt);
        return subscribableIdentifiedObjectEntity;
     }

     public ResponseEntity<Map<String, Object>>getAllDerPrograms() {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<DERProgramEntity> derEntityList = derProgramRepository.findAll();
            List<SubscribableIdentifiedObjectEntity> SubscribableIdentifiedObjectList = subscribableIdentifiedObjectRepository.findAll();
            List<Map<String, Object>> fsaDetails = derEntityList.stream()
                .map(derEntity -> {
                    Map<String, Object> entityMap = new HashMap<>();
                    entityMap.put("id", derEntity .getId());
                    entityMap.put("primacy", derEntity.getPrimacy());
                    if (derEntity.getDefaultDERControlLink()!= null) {
                        entityMap.put("defaultDERControlLink", derEntity.getDefaultDERControlLink());
                    }
                    if (derEntity.getActiveDERControlListLink()!= null) {
                        entityMap.put("activeDERControlListLink", derEntity.getActiveDERControlListLink());
                    }
                    if (derEntity.getDERControlListLink() != null) {
                        entityMap.put("derControlListLink", derEntity.getDERControlListLink());
                    }
                    if (derEntity.getDERCurveListLink() != null) {
                        entityMap.put("derCurveListLink", derEntity.getDERCurveListLink());
                    }
                    return entityMap;
                })
                .collect(Collectors.toList());
            List<Map<String, Object>> subscribableIdentifiedObjectDetails = SubscribableIdentifiedObjectList.stream()
                .map(subscribableIdentifiedObject -> {
                    Map<String, Object> entityMap = new HashMap<>();
                    entityMap.put("mRID", subscribableIdentifiedObject.getmRID() != null ? subscribableIdentifiedObject.getmRID() : "N/A");
                    entityMap.put("description", subscribableIdentifiedObject.getDescription() != null ? subscribableIdentifiedObject.getDescription() : "No description");
                    entityMap.put("version", subscribableIdentifiedObject.getVersion());
                    return entityMap;
                })
                .collect(Collectors.toList());
            List<Map<String, Object>> responseList = new ArrayList<>(fsaDetails);
            responseList.addAll(subscribableIdentifiedObjectDetails);
            
            if (fsaDetails.isEmpty() && subscribableIdentifiedObjectDetails.isEmpty()) {
                responseMap.put("message", "No functionSetAssignments found.");
            } else {
                responseMap.put("DERPrograms", responseList);
            }
    
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving functionSetAssignments", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    public String getAllDerProgramsHttp() {
        try {
            List<DERProgramEntity> derEntityList = derProgramRepository.findAll();

            if (derEntityList.isEmpty()) {
                String emptyXml = "<DERProgramList xmlns=\"http://ieee.org/2030.5\" " +
                    "all=\"0\" href=\"/derp\" results=\"0\">\n" +
                    "<message>No DERPrograms found</message>\n" +
                    "</DERProgramList>";
                return emptyXml;
            }

            StringBuilder xml = new StringBuilder();
            xml.append("<DERProgramList xmlns=\"http://ieee.org/2030.5\" ")
               .append("all=\"").append(derEntityList.size()).append("\" ")
               .append("href=\"").append("/derp").append("\" ")
               .append("results=\"").append(derEntityList.size()).append("\">\n");

            for (DERProgramEntity der : derEntityList) {
                xml.append(" <DERProgram href=\"").append(stripHost(der.getFunctionSetAssignmentsEntity().getDERProgramListLink())).append("/").append(der.getId()).append("\">\n");

                // Core attributes from SubscribableIdentifiedObjectEntity
                SubscribableIdentifiedObjectEntity subscribable = der.getSubscribableIdentifiedObject();
                if (subscribable != null) {
                    xml.append("  <mRID>")
                       .append(subscribable.getmRID() != null ? subscribable.getmRID() : "N/A")
                       .append("</mRID>\n");
                    xml.append("  <description>")
                       .append(subscribable.getDescription() != null ? subscribable.getDescription() : "No description")
                       .append("</description>\n");
                }

                // Map of fields to XML tag names
                Map<String, String> listLinks = Map.of(
                    "getDefaultDERControlLink", "DefaultDERControlLink",
                    "getActiveDERControlListLink", "ActiveDERControlListLink",
                    "getDERControlListLink", "DERControlListLink",
                    "getDERCurveListLink", "DERCurveListLink"
                );

                for (Map.Entry<String, String> entry : listLinks.entrySet()) {
                    try {
                        Method method = DERProgramEntity.class.getMethod(entry.getKey());
                        Object value = method.invoke(der);
                        if (value != null) {
                            xml.append("  <").append(entry.getValue()).append(" href=\"")
                               .append(stripHost(value.toString())).append("\" all=\"0\"/>\n");
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                        LOGGER.log(Level.WARNING, "Error processing method " + entry.getKey(), e);
                    }
                }
                // DERProgram specific attributes
                xml.append("  <primacy>").append(der.getPrimacy() != null ? der.getPrimacy() : 0).append("</primacy>\n");

                xml.append(" </DERProgram>\n");
            }

            xml.append("</DERProgramList>");
            return xml.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERPrograms", e);
            return "<DERProgramList xmlns=\"http://ieee.org/2030.5\" all=\"0\" href=\""+"/derp\" results=\"0\">\n" +
                   "<error>Some error occurred</error>\n" +
                   "</DERProgramList>";
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


  
    public ResponseEntity<Map<String, Object>> getDerProgram(Long derId)

    {
        try {
            Map<String, Object> result = new HashMap<>();
           Optional <DERProgramEntity> derEntityOptional = derProgramRepository.findById(derId);
           Optional <SubscribableIdentifiedObjectEntity> subscribableIdentifiedObjectOptional = subscribableIdentifiedObjectRepository.findById(derId);
           DERProgramEntity derEntity = derEntityOptional.get();
           SubscribableIdentifiedObjectEntity subscribableIdentifiedEntity = subscribableIdentifiedObjectOptional.get();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", derEntity .getId());
            entityMap.put("primacy", derEntity.getPrimacy());
            entityMap.put("defaultDERControlLink", derEntity.getDefaultDERControlLink());
            entityMap.put("activeDERControlListLink", derEntity.getActiveDERControlListLink());
            entityMap.put("derControlListLink", derEntity.getDERControlListLink());
            entityMap.put("derCurveListLink", derEntity.getDERCurveListLink());
            entityMap.put("mRID", subscribableIdentifiedEntity .getmRID() != null ? subscribableIdentifiedEntity.getmRID() : "N/A");
            entityMap.put("description", subscribableIdentifiedEntity.getDescription() != null ? subscribableIdentifiedEntity.getDescription() : "No description");
           // entityMap.put("subscribable", subscribableIdentifiedEntity.getSubscribable());
            entityMap.put("version", subscribableIdentifiedEntity .getVersion());
            result.put("DERProgram", entityMap);
            return ResponseEntity.ok( result );
          } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving RegisteredEndDevice", e);
            return ResponseEntity.status(404).body(null);
        }   
       
    }

    public String getDerProgramHttp(Long derProgramId) {
        try {
            Optional<DERProgramEntity> derProgramEntityOptional = derProgramRepository.findById(derProgramId);

            if (derProgramEntityOptional.isEmpty()) {
                return "<DERProgram xmlns=\"http://ieee.org/2030.5\" href=\"/derp/" + derProgramId + "\">\n" +
                       "<message>No DERProgram found for DERProgram ID " + derProgramId + "</message>\n" +
                       "</DERProgram>";
            }

            DERProgramEntity der = derProgramEntityOptional.get();
            StringBuilder xml = new StringBuilder();
            xml.append("<DERProgram xmlns=\"http://ieee.org/2030.5\" ")
               .append("href=\"").append(stripHost(der.getFunctionSetAssignmentsEntity().getDERProgramListLink())).append("/").append(der.getId()).append("\">\n");

            // Core attributes from SubscribableIdentifiedObjectEntity
            SubscribableIdentifiedObjectEntity subscribable = der.getSubscribableIdentifiedObject();
            if (subscribable != null) {
                xml.append(" <mRID>")
                   .append(subscribable.getmRID() != null ? subscribable.getmRID() : "N/A")
                   .append("</mRID>\n");
                xml.append(" <description>")
                   .append(subscribable.getDescription() != null ? subscribable.getDescription() : "No description")
                   .append("</description>\n");
            }

            // Map of fields to XML tag names
            Map<String, String> listLinks = Map.of(
                "getDefaultDERControlLink", "DefaultDERControlLink",
                "getActiveDERControlListLink", "ActiveDERControlListLink",
                "getDERControlListLink", "DERControlListLink",
                "getDERCurveListLink", "DERCurveListLink"
            );

            for (Map.Entry<String, String> entry : listLinks.entrySet()) {
                try {
                    Method method = DERProgramEntity.class.getMethod(entry.getKey());
                    Object value = method.invoke(der);
                    if (value != null) {
                        xml.append(" <").append(entry.getValue()).append(" href=\"")
                           .append(stripHost(value.toString())).append("\"");
                        if (entry.getValue().endsWith("ListLink")) {
                            xml.append(" all=\"0\"");
                        }
                        xml.append("/>\n");
                    }
                } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                    LOGGER.log(Level.WARNING, "Error processing method " + entry.getKey(), e);
                }
            }

            // DERProgram specific attributes
            xml.append(" <primacy>").append(der.getPrimacy() != null ? der.getPrimacy() : "0").append("</primacy>\n");

            xml.append("</DERProgram>");
            return xml.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving DERProgram", e);
            return "<DERProgram xmlns=\"http://ieee.org/2030.5\" href=\"/sep2/A1/derp/" + derProgramId + "\">\n" +
                   "<error>Some error occurred</error>\n" +
                   "</DERProgram>";
        }
    }

 
}



 
 


/*
 *  
 * 
 * 
 * 
 */












