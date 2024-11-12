package interstore.FunctionSetAssignments;
import interstore.EndDevice.EndDeviceDto;
import interstore.EndDevice.EndDeviceRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class FunctionSetAssignmentsService {
    @Autowired
    FunctionSetAssignmentsRepository functionSetAssignmentsRepository;
    
    @Autowired
    private EndDeviceRepository endDeviceRepository;

    private static final Logger LOGGER = Logger.getLogger(FunctionSetAssignmentsService.class.getName());
     
     @Transactional
    public FunctionSetAssignmentsEntity createFunctionsetAssignments(JSONObject payload) throws NumberFormatException, JSONException, NotFoundException {
        Long endDeviceId = Long.parseLong(payload.getJSONObject("payload").getString("endDeviceId"));
        EndDeviceDto endDevice = endDeviceRepository.findById( endDeviceId)
        .orElseThrow(() -> new NotFoundException());
        //endDevice.getFunctionSetAssignmentsListLink();
        String fsaLink = endDevice.getFunctionSetAssignmentsListLink();
        FunctionSetAssignmentsEntity fsaEntity = new FunctionSetAssignmentsEntity(); 
        fsaEntity.setEndDevice(endDevice); 
        try {
            fsaEntity  = functionSetAssignmentsRepository.save(fsaEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving FSA entity", e);
        }
        setFunctionSetAssignmentEntity(fsaEntity  , payload,  fsaLink );
        fsaEntity  = functionSetAssignmentsRepository.save(fsaEntity);
        return fsaEntity;   
    }

    
    public void setFunctionSetAssignmentEntity(FunctionSetAssignmentsEntity fsaEntity , JSONObject payload, String functionsetassignmentLink) throws NotFoundException
      {    
        Long fsaId = fsaEntity.getId();
        JSONObject Fsapayload = payload.optJSONObject("payload");
        String idString = "/"+ String.valueOf(fsaId) ;
        String fsaLink = functionsetassignmentLink + idString;   
        String subScribable = Fsapayload.optString("subscribable");
        short shortSubscribable = Short.parseShort(subScribable);
        String mRID = Fsapayload.optString("mRID");
        String version = Fsapayload.optString("version");
        int intVersion = Integer.parseInt(version);
        String description = Fsapayload.optString("description");
        fsaEntity.setSubscribable(shortSubscribable);
        fsaEntity.setmRID(mRID);
        fsaEntity.setVersion(intVersion );
        fsaEntity.setDescription(description);
        fsaEntity.setFunctionSetAssignmentsLink(fsaLink);
        findListLink(Fsapayload, fsaEntity);
        AddSubscribabaleFsa(shortSubscribable, fsaEntity);
      }
     
    public  void findListLink(JSONObject payload, FunctionSetAssignmentsEntity fsaEntity)
    {   
        try {
            Iterator<String> keys  = payload.keys();
            while(keys.hasNext())
            {
                String key = keys.next();
                String value = payload.optString(key);
                if(!value.isEmpty())
                {
                    switch(key) {
                        case "demandResponseProgramListLink":
                        fsaEntity.setDemandResponseProgramListLink(payload.optString(key));
                        break;
                        case "fileListLink":
                        fsaEntity.setFileListLink(payload.optString(key));
                        break;
                        case "traiffProfileListLink":
                        fsaEntity.setTariffProfileListLink(payload.optString(key));
                        break;
                        case "messagingProgramListLink":
                        fsaEntity.setMessagingProgramListLink(payload.optString(key));
                        break;
                        case "usagePointListLink":
                        fsaEntity.setUsagePointListLink(payload.optString(key));
                        break;
                        case "dERProgramListLink":
                        fsaEntity.setDERProgramListLink(payload.optString(key));
                        LOGGER.info(fsaEntity.getDERProgramListLink().toString()); 
                        break;
                        case "customerAccountListLink":
                        fsaEntity.setCustomerAccountListLink(payload.optString(key));
                        break;
                        case "prepaymentListLink":
                        fsaEntity.setPrepaymentListLink(payload.optString(key));
                        break;
                        case "responseSetListLink":
                        fsaEntity.setResponseSetListLink(payload.optString(key));
                         default:
                            break;    
            }
                }
              
        }
    }
         catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving EndDeviceDto", e);
        }
       
    
}
   
public ResponseEntity<Map<String, Object>> getAllFunctionsetAssignments(Long endDeviceId) {
    try {
        Map<String, Object> responseMap = new HashMap<>();
        List<FunctionSetAssignmentsEntity> fsaEntityList = functionSetAssignmentsRepository.findByEndDeviceId(endDeviceId);

        List<Map<String, Object>> fsaDetails = fsaEntityList.stream()
            .map(fsaEntity -> {
                Map<String, Object> entityMap = new HashMap<>();
                entityMap.put("id", fsaEntity.getId());
                entityMap.put("mRID", fsaEntity.getmRID() != null ? fsaEntity.getmRID() : "N/A");
                entityMap.put("description", fsaEntity.getDescription() != null ? fsaEntity.getDescription() : "No description");
                entityMap.put("subscribable", fsaEntity.getSubscribable());
                entityMap.put("version", fsaEntity.getVersion());
                if (fsaEntity.getDemandResponseProgramListLink() != null) {
                    entityMap.put("demandResponseProgramListLink", fsaEntity.getDemandResponseProgramListLink());
                }
                if (fsaEntity.getFileListLink() != null) {
                    entityMap.put("fileListLink", fsaEntity.getFileListLink());
                }
                if (fsaEntity.getTariffProfileListLink() != null) {
                    entityMap.put("tariffProfileListLink", fsaEntity.getTariffProfileListLink());
                }
                if (fsaEntity.getMessagingProgramListLink() != null) {
                    entityMap.put("messagingProgramListLink", fsaEntity.getMessagingProgramListLink());
                }
                if (fsaEntity.getUsagePointListLink() != null) {
                    entityMap.put("usagePointListLink", fsaEntity.getUsagePointListLink());
                }
                if (fsaEntity.getDERProgramListLink() != null) {
                    entityMap.put("dERProgramListLink", fsaEntity.getDERProgramListLink());
                }
                if (fsaEntity.getCustomerAccountListLink() != null) {
                    entityMap.put("customerAccountListLink", fsaEntity.getCustomerAccountListLink());
                }
                if (fsaEntity.getPrepaymentListLink() != null) {
                    entityMap.put("prepaymentListLink", fsaEntity.getPrepaymentListLink());
                }
                if (fsaEntity.getResponseSetListLink() != null) {
                    entityMap.put("responseSetListLink", fsaEntity.getResponseSetListLink());
                }
                if (fsaEntity.getFunctionSetAssignmentsLink() != null) {
                    entityMap.put("functionSetAssignmentsLink", fsaEntity.getFunctionSetAssignmentsLink());
                }

                return entityMap;
            })
            .collect(Collectors.toList());

        if (fsaDetails.isEmpty()) {
            responseMap.put("message", "No functionSetAssignments found.");
        } else {
            responseMap.put("functionSetAssignments", fsaDetails);
        }

        return ResponseEntity.ok(responseMap);
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error retrieving functionSetAssignments", e);
        return ResponseEntity.status(404).body(null);
    }
}

    public void AddSubscribabaleFsa(Short shortSubscribable,FunctionSetAssignmentsEntity fsaEntity )
        {
          if(shortSubscribable!= 0)
          {
            fsaEntity.setFSASubscribableList(fsaEntity);
          }
        }

     /*get a single function set assignments for an end device  */
    @SuppressWarnings("unused")
    public ResponseEntity<Map<String, Object>> getFunctionsetAssignments(Long endDeviceId, Long fsaId)

    {
        try {
            Map<String, Object> result = new HashMap<>();
           Optional <FunctionSetAssignmentsEntity> fsaEntityOptional = functionSetAssignmentsRepository.findFirstByEndDeviceIdAndId( endDeviceId , fsaId );
           FunctionSetAssignmentsEntity fsaEntity = fsaEntityOptional.get();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", fsaEntity.getId());
            entityMap.put("mRID", fsaEntity.getmRID() != null ? fsaEntity.getmRID() : "N/A");
            entityMap.put("description", fsaEntity.getDescription() != null ? fsaEntity.getDescription() : "No description");
            entityMap.put("subscribable", fsaEntity.getSubscribable());
            entityMap.put("version", fsaEntity.getVersion());

            if (fsaEntity.getDemandResponseProgramListLink() != null) {
                entityMap.put("demandResponseProgramListLink", fsaEntity.getDemandResponseProgramListLink());
            }
            if (fsaEntity.getFileListLink() != null) {
                entityMap.put("fileListLink", fsaEntity.getFileListLink());
            }
            if (fsaEntity.getTariffProfileListLink() != null) {
                entityMap.put("tariffProfileListLink", fsaEntity.getTariffProfileListLink());
            }
            if (fsaEntity.getMessagingProgramListLink() != null) {
                entityMap.put("messagingProgramListLink", fsaEntity.getMessagingProgramListLink());
            }
            if (fsaEntity.getUsagePointListLink() != null) {
                entityMap.put("usagePointListLink", fsaEntity.getUsagePointListLink());
            }
            if (fsaEntity.getDERProgramListLink() != null) {
                entityMap.put("dERProgramListLink", fsaEntity.getDERProgramListLink());
            }
            if (fsaEntity.getCustomerAccountListLink() != null) {
                entityMap.put("customerAccountListLink", fsaEntity.getCustomerAccountListLink());
            }
            if (fsaEntity.getPrepaymentListLink() != null) {
                entityMap.put("prepaymentListLink", fsaEntity.getPrepaymentListLink());
            }
            if (fsaEntity.getResponseSetListLink() != null) {
                entityMap.put("responseSetListLink", fsaEntity.getResponseSetListLink());
            }
            if (fsaEntity.getFunctionSetAssignmentsLink() != null) {
                entityMap.put("functionSetAssignmentsLink", fsaEntity.getFunctionSetAssignmentsLink());
            }


            if (fsaEntity == null) {
                result.put("message", "No functionsetassignment found for EndDevice ID " +  endDeviceId  + " and FSA ID " +   fsaId );
            } else {
                result.put("FunctionSetAssignments", entityMap);
            }
            return ResponseEntity.ok( result );
          } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving RegisteredEndDevice", e);
            return ResponseEntity.status(404).body(null);
        }

       
       
    }


}


