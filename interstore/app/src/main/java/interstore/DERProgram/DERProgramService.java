package interstore.DERProgram;
import interstore.EndDevice.EndDeviceDto;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsEntity;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsRepository;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
import interstore.Identity.*;
import interstore.Types.*; 
import jakarta.transaction.Transactional;

import org.json.JSONArray;
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
   

      /* here i expect a json string here 
       *  payload = {
       *  "derpListLink": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
       *   ActiveDERControlListLink .subscribable is short 
       *}
       * 
       *  {"payload" : { "subscribable" : "", "mRID": "", 
       *  "description": "", "version": "", "activeDERControlListLink": "", "defaultDERControlLink": ""
       *  "dERControlListLink": "", "dERCurveListLink": "", " primacy" : "" ,  "derpLink" : "" 
       *  "fsaId" : ""   
       * }}
       *  here i have to get first corresponding end device and function set assignment 
       */
    @Transactional
    public DERProgramEntity createDerProgram(JSONObject payload )  throws NumberFormatException, JSONException, NotFoundException{
        JSONObject DerProgrampayload = payload.optJSONObject("payload");
        short subscribable = (short) DerProgrampayload.optInt("subscribable");
        String mRID = DerProgrampayload.optString("mRID");
        String description = DerProgrampayload.optString("description");
        String version = DerProgrampayload.optString("version");
        Integer versionInt = Integer.parseInt(version );
        String primacy = DerProgrampayload.optString("primacy"); 
        Short primacyShort = Short.parseShort(primacy);
        Long fsaId = (long) DerProgrampayload.optInt("fsaId");
        String activeDERControlListLink = DerProgrampayload.optString("activeDERControlListLink");
        String defaultDERControlLink = DerProgrampayload.optString("defaultDERControlLink");
        String dERControlListLink = DerProgrampayload.optString("dERControlListLink");
        String dERCurveListLink = DerProgrampayload.optString("dERCurveListLink");
       
        SubscribableIdentifiedObjectEntity subscribableIdentifiedObjectEntity = new SubscribableIdentifiedObjectEntity();
        subscribableIdentifiedObjectRepository.save(subscribableIdentifiedObjectEntity);
        subscribableIdentifiedObjectEntity = setSubscribableIdentifiedObject(subscribableIdentifiedObjectEntity, mRID, description, versionInt);
        subscribableIdentifiedObjectRepository.save(subscribableIdentifiedObjectEntity);

        SubscribableResourceEntity subscribableResourceEntity = new SubscribableResourceEntity();
        subscribableResourcRepository.save(subscribableResourceEntity);
        subscribableResourceEntity.setSubscribable(subscribable);
        subscribableResourcRepository.save(subscribableResourceEntity); 
        

        DERProgramEntity derProgram = new DERProgramEntity();
        derProgramRepository.save(derProgram);
        derProgram.setPrimacy(primacyShort);
        Optional<FunctionSetAssignmentsEntity> fsaEntityOptional = functionSetAssignmentsRepository.findById(fsaId);
        if (fsaEntityOptional.isPresent()) {
        FunctionSetAssignmentsEntity fsaEntity = fsaEntityOptional.get();
        String derListLink = fsaEntity.getDERProgramListLink(); 
        derProgram.setFunctionSetAssignmentEntity(fsaEntity);
        LOGGER.log(Level.INFO, "DER Program List Link: " + derListLink);
       } else {
        LOGGER.log(Level.SEVERE, "Function Set Assignments Entity not found for ID: " + fsaId);  
        }
        derProgram.setActiveDERControlListLink(activeDERControlListLink);
        derProgram.setDefaultDERControlLink(defaultDERControlLink);
        derProgram.setDERControlListLink(dERControlListLink);
        derProgram.setDERCurveListLink(dERCurveListLink);
        derProgram.setSubscribableIdentifiedObject( subscribableIdentifiedObjectEntity);
        derProgram.setSubscribableResource( subscribableResourceEntity);
        derProgramRepository.save(derProgram); 
        
        return derProgram;
    }
    
     public SubscribableIdentifiedObjectEntity  setSubscribableIdentifiedObject (SubscribableIdentifiedObjectEntity subscribableIdentifiedObjectEntity,
     String mRID, String description , Integer versionInt ){
        subscribableIdentifiedObjectEntity.setmRID(mRID); //   setmRI(mRID);
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