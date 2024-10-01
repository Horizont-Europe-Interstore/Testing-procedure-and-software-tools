package interstore.DERProgram;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsEntity;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsRepository;
import interstore.Identity.*;
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
    SubscribableResourceRepository subscribableResourcRepository;

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
            subscribableResourcRepository.save(subscribableResourceEntity);
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
        subscribableIdentifiedObjectEntity = setSubscribableIdentifiedObject(subscribableIdentifiedObjectEntity, mRID, description, versionInt);
        subscribableIdentifiedObjectRepository.save(subscribableIdentifiedObjectEntity);
    
        subscribableResourceEntity.setSubscribable(subscribable);
        subscribableResourcRepository.save(subscribableResourceEntity);
    
        String derListLink = fsaEntity.getDERProgramListLink();
        LOGGER.log(Level.INFO, "DER Program List Link: " + derListLink);
        LOGGER.log(Level.INFO, "Function Set Assignments Entity: " + fsaEntity);
    
        derProgram.setFunctionSetAssignmentEntity(fsaEntity);

        Long derId = derProgram.getId();
        derProgram.setPrimacy(primacyShort);
    
        String idString = derListLink + "/" + String.valueOf(derId) + "/";
    
        derProgram.setActiveDERControlListLink(activeDERControlListLink != null ? idString + activeDERControlListLink : null);
        derProgram.setDefaultDERControlLink(defaultDERControlLink != null ? idString + defaultDERControlLink : null);
        derProgram.setDERControlListLink(dERControlListLink != null ? idString + dERControlListLink : null);
        derProgram.setDERCurveListLink(dERCurveListLink != null ? idString + dERCurveListLink : null);
    
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

     public ResponseEntity<Map<String, Object>>getAllDerPrograms(Long fsaID) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<DERProgramEntity> derEntityList = derProgramRepository.findByFsaEntity_Id(fsaID);
            List<SubscribableIdentifiedObjectEntity> SubscribableIdentifiedObjectList = subscribableIdentifiedObjectRepository.findByFsaEntity_Id(fsaID);
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
                responseMap.put("functionSetAssignments", responseList);
            }
    
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving functionSetAssignments", e);
            return ResponseEntity.status(404).body(null);
        }
    }



   public ResponseEntity<Map<String, Object>> getDerProgramById(Long id) {
        DERProgramEntity derProgram = derProgramRepository.findById(id).orElse(null);
        Map<String, Object> response = new HashMap<>();
        response.put("derProgram", derProgram);
        return ResponseEntity.ok(response);
    }
    
}



 
 













