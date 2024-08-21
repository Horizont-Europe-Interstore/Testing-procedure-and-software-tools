package interstore.FunctionSetAssignments;
import interstore.EndDevice.EndDeviceDto;
import interstore.EndDevice.EndDeviceImpl; 
import interstore.EndDevice.EndDeviceRepository;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private EndDeviceImpl endDeviceImpl;

    @Autowired
    private EndDeviceRepository endDeviceRepository;

    private static final Logger LOGGER = Logger.getLogger(FunctionSetAssignmentsService.class.getName());
     
    @Transactional
    public FunctionSetAssignmentsEntity createFunctionsetAssignments(JSONObject payload) throws NumberFormatException, JSONException {
        LOGGER.info("The payload reached FSA service class: " + payload);
        Long endDeviceId = Long.parseLong(payload.getJSONObject("payload").getString("endDeviceId"));
        Optional<EndDeviceDto> endDeviceOptional = endDeviceRepository.findById(endDeviceId);
        FunctionSetAssignmentsEntity fsaEntity = new FunctionSetAssignmentsEntity();
        
        // Save it to the database
        try {
            if(endDeviceOptional.isPresent()) {
                fsaEntity.setEndDevice(endDeviceOptional.get());
            } else {
                LOGGER.warning("End device with ID " + endDeviceId + " not found.");
            }
            fsaEntity = functionSetAssignmentsRepository.save(fsaEntity);
            LOGGER.info("FSA entity saved: " + fsaEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving FSA entity", e);
        }
        
        // Set properties and save again
        setFunctionSetAssignmentEntity(fsaEntity, payload);
        try {
            fsaEntity = functionSetAssignmentsRepository.save(fsaEntity);
            LOGGER.info("FSA entity updated and saved: " + fsaEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating and saving FSA entity", e);
        }
        
        return fsaEntity;
    }
    

    
     /*
      @Transactional
    public FunctionSetAssignmentsEntity createFunctionsetAssignments(JSONObject  payload)
    {
        LOGGER.info("the payload reached fsa service class " +  payload);
        FunctionSetAssignmentsEntity fsaEntity = new FunctionSetAssignmentsEntity();
        LOGGER.info("the fsa enttiy class is here" + fsaEntity ); 
        fsaEntity = functionSetAssignmentsRepository.save(fsaEntity);
        setFunctionSetAssignmentEntity(fsaEntity, payload);
        fsaEntity = functionSetAssignmentsRepository.save(fsaEntity);
        return fsaEntity ;
       
    }



      * 
       



      
        "payload" : {
         mRID : "XXXXXXXX", type: mRIDType
        "description" : "FSA description",  type : String32 
        "subscribable" : "1",   type: SubscribabaleType 
        "version" : "0",  type: VersionType 
        "functionsetassignmentLink" : "http://localhost:8080/edev/id/fsa"
        "DemandResponseProgramListLink": "/dr"
        "FileListLink": "/file",
        "TraiffProfileListLink": "/tp",
        "MessagingProgramListLink": "/msg",
        "UsagePointListLink": "/upt",
        "DERProgramListLink": "/derp",
        "CustomerAccountListLink":"/bill",
        "PrepaymentListLink": "/ppy",
        "ResponseSetListLink" "/rsps"
        "EndDeviceID": "1"  type: EndDeviceIDType 

        }
       
        if i receive the end device id in the payload, i need to create the fsa link
         i need to create a function set assignments for a particular end device for this 
         reason i have to choose the end device id from front end and put it in the payload 
         once i receive the end device id i have to query for it's corresponding uri and it's 
         fsa uri .i can create the fsa link 
         "functionSetAssignmentsListLink":"http://localhost/edev/1/fsa"



              mRIDType mRid = new mRIDType( Fsapayload.optString("mRID"));

          
            String subScribabale = Fsapayload.optString("subscribable");
            short shortSubscribable = Short.parseShort(subScribabale);
            SubscribableType subscribableType =  new SubscribableType(shortSubscribable) ; 
            String version = Fsapayload.optString("version");
            int intVersion = Integer.parseInt(version) ;
            VersionType versionType = new VersionType(intVersion);

              fsaEntity.setmRID(mRid);
            fsaEntity.setDescription(description);
            fsaEntity.setSubscribable(subscribableType);
            fsaEntity.setVersion(versionType);

      */
    public void setFunctionSetAssignmentEntity(FunctionSetAssignmentsEntity fsaEntity , JSONObject payload)
      {    
        LOGGER.info("the received payload in the FSA Service class  is " + payload);
            Long fsaId = fsaEntity.getId();
            LOGGER.info("the fsa id is in service class  " + fsaId); 
            JSONObject Fsapayload = payload.optJSONObject("payload");
            LOGGER.info("the json paylaod is the service class is" + Fsapayload);
            String endDeviceId = Fsapayload.optString("endDeviceId");
            Long endDeviceIdLong = Long.parseLong(endDeviceId); 
            LOGGER.info("the end device id is in service class  " + endDeviceIdLong);
            String idString = "/"+ String.valueOf(fsaId) ;
            String functionsetassignmentLink = createFunctionSetAssignmentsLink(endDeviceIdLong );
            String fsaLink = functionsetassignmentLink + idString;
            LOGGER.info("the fsa link is in service class ööö " + fsaLink); 
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
            LOGGER.info(fsaEntity.getDescription());
            LOGGER.info(fsaEntity.getmRID().toString());
            LOGGER.info(fsaEntity.getSubscribable().toString());
            LOGGER.info(fsaEntity.getVersion().toString());
            LOGGER.info(fsaEntity.getFunctionSetAssignmentsLink().toString()); 
            findListLink(Fsapayload, fsaEntity);
            AddSubscribabaleFsa(shortSubscribable, fsaEntity);

        
      }
     
     
      public String createFunctionSetAssignmentsLink(Long id) {
        try {
            ResponseEntity<Map<String, Object>> responseEntity = endDeviceImpl.getEndDevice(id);
            Map<String, Object> endDeviceMap = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            
            for (Map.Entry<String, Object> entry : endDeviceMap.entrySet()) {
                try {
                    String jsonValue = objectMapper.writeValueAsString(entry.getValue());
                    Map<String, String> FsaLinkMap = objectMapper.readValue(jsonValue , new TypeReference<Map<String, String>>(){});
                    if (FsaLinkMap.containsKey("functionSetAssignmentsListLink")) {
                        LOGGER.info("the fsaLink value is " + FsaLinkMap.get("functionSetAssignmentsListLink"));
                       return FsaLinkMap.get("functionSetAssignmentsListLink");
                    }
                   
                } catch (JsonProcessingException e) {
                    LOGGER.info("Error occurred while converting to JSON: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving EndDeviceDto", e);
        }
        return null;
    }







    public  void findListLink(JSONObject payload, FunctionSetAssignmentsEntity fsaEntity)
    {   
        LOGGER.info("the payload is displayed in the findListLink method " + payload);
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

                // Add only non-null links to the response
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



    
    /*
     * if the function set assignment is subscribabsale then it has 
     * the function set assignemnts created has to added to the database 
     * so there is a need for member called functionsetassignmentsubscribabale 
     * 
     * 
     */
}







/*

 * 
 * 
 * 
 */