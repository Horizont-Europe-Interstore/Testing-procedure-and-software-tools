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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String activeDERControlListLink = DerProgrampayload.optString("activeDERControlListLink", null);  // Default to null if missing
        String defaultDERControlLink = DerProgrampayload.optString("defaultDERControlLink", null);        // Default to null if missing
        String dERControlListLink = DerProgrampayload.optString("dERControlListLink", null);              // Default to null if missing
        String dERCurveListLink = DerProgrampayload.optString("dERCurveListLink", null);                  // Default to null if missing
    
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

   public ResponseEntity<Map<String, Object>> getAllDerPrograms() {
        List<DERProgramEntity> derPrograms = derProgramRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("derPrograms", derPrograms);
        return ResponseEntity.ok(response);
    }

   public ResponseEntity<Map<String, Object>> getDerProgramById(Long id) {
        DERProgramEntity derProgram = derProgramRepository.findById(id).orElse(null);
        Map<String, Object> response = new HashMap<>();
        response.put("derProgram", derProgram);
        return ResponseEntity.ok(response);
    }
}


 /* here i expect a json string here 
       * the received payload in the DER program Manager class  is {"payload":{"activeDERControlListLink":"actderc","mRID":"B01000000","defaultDERControlLink":"dderc",
       * "dERCurveListLink":"dc","derpLink":"derp","description":"der program fsa","fsaID":"1","primacy":"89","subscribable":"1","version":"1","dERControlListLink":"derc"},
       * "action":"post","servicename":"createDerprogrammanager"}
       *  here i have to get first corresponding end device and function set assignment 
       
    @Transactional
    public DERProgramEntity createDerProgram(JSONObject payload )  throws NumberFormatException, JSONException, NotFoundException{
        Long fsaId = Long.parseLong(payload.getJSONObject("payload").getString("fsaID"));
        FunctionSetAssignmentsEntity fsaEntity = functionSetAssignmentsRepository.findFsaById(fsaId);
        DERProgramEntity derProgram = new DERProgramEntity();
        derProgram.setFunctionSetAssignmentEntity(fsaEntity);
        SubscribableIdentifiedObjectEntity subscribableIdentifiedObjectEntity = new SubscribableIdentifiedObjectEntity();
        SubscribableResourceEntity subscribableResourceEntity = new SubscribableResourceEntity(); 
        try {
            
            derProgramRepository.save(derProgram); 
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving der program entity", e);
        }
        try {
            subscribableIdentifiedObjectRepository.save(subscribableIdentifiedObjectEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving subscribableIdentifiedObjectEntity entity", e);
        }
        try{
            subscribableResourcRepository.save(subscribableResourceEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving subscribableResourceEntity entity", e);
        }
        derProgram = setDerProgramEntity(payload, derProgram, subscribableIdentifiedObjectEntity,
         subscribableResourceEntity, fsaEntity);
        return derProgram;
    }
    
    
    public DERProgramEntity setDerProgramEntity(JSONObject payload, DERProgramEntity derProgram, 
    SubscribableIdentifiedObjectEntity subscribableIdentifiedObjectEntity,
    SubscribableResourceEntity subscribableResourceEntity , FunctionSetAssignmentsEntity fsaEntity )
    {
      
        JSONObject DerProgrampayload = payload.optJSONObject("payload");
        short subscribable = (short) DerProgrampayload.optInt("subscribable");
        String mRID = DerProgrampayload.optString("mRID");
        String description = DerProgrampayload.optString("description");
        String version = DerProgrampayload.optString("version");
        Integer versionInt = Integer.parseInt(version );
        String primacy = DerProgrampayload.optString("primacy"); 
        Short primacyShort = Short.parseShort(primacy);
        String activeDERControlListLink = DerProgrampayload.optString("activeDERControlListLink");
        String defaultDERControlLink = DerProgrampayload.optString("defaultDERControlLink");
        String dERControlListLink = DerProgrampayload.optString("dERControlListLink");
        String dERCurveListLink = DerProgrampayload.optString("dERCurveListLink");
    
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
            String idString = derListLink + "/"+ String.valueOf(derId) ;
            derProgram.setActiveDERControlListLink(idString + activeDERControlListLink);
            derProgram.setDefaultDERControlLink(idString + defaultDERControlLink);
            derProgram.setDERControlListLink(idString + dERControlListLink);
            derProgram.setDERCurveListLink(idString + dERCurveListLink);
            derProgram.setSubscribableIdentifiedObject( subscribableIdentifiedObjectEntity);
            derProgram.setSubscribableResource( subscribableResourceEntity);
            derProgramRepository.save(derProgram); 
            return derProgram ;
            
        }
       
    */





/*  public void setDERP(DERProgramEntity derProgram){
        derProgram.setmRID("B01000000");
        derProgram.setPrimacy("89");
        derProgram.setDescription("SUBTX-A1-B1");
        derProgram.setVersion("1");
        derProgram.setSubscribable("0");
    }
        public String getDerProgramByListLinks(Object derpListLinks) throws JSONException {
        Map<Long, Object> derProgramMap = new HashMap<>();
        if (derpListLinks instanceof JSONArray) {
            JSONArray linksArray = (JSONArray) derpListLinks;

            for (int i = 0; i < linksArray.length(); i++) {
                String link = linksArray.getString(i);
                List<DERProgram> values = derProgramRepository.findAllByDerpListLink(link);
                for (DERProgramEntity derProgram : values) {
                    derProgramMap.put(derProgram.getId(), derProgram.getAll());
                }
            }
        }
        return derProgramMap.toString();
    } 
    
    */






/*
 * public void createDERP(DERPList derpList){
        DERProgram derProgram = new DERProgram(derpList);
        setDERP(derProgram);
        derProgram = derProgramRepository.save(derProgram);
        derpList.addDerpDto(derProgram);
        setDERPLinks(derProgram, derpList);
    }
 * 
 * 
    public void setDERPLinks(DERProgram derProgram, DERPList derpList){
        String derpListLink = derpList.getDerpListLink();
        Long id = derProgram.getId();
        derProgram.setDefaultDERControlLink(derpListLink+"/"+id.toString()+"/dderc");
        derProgram.setActiveDERControlListLink(derpListLink+"/"+id.toString()+"/actderc");
        derProgram.setDERControlListLink(derpListLink+"/"+id.toString()+"/derc");
        derProgram.setDERCurveListLink(derpListLink+"/"+id.toString()+"/dc");
        derProgram.setDerpLink(derpListLink+"/"+id.toString());
        derProgram.setDerpListLink(derpListLink);
    }

 * 
 * 
 * 
 * 
 */