package interstore.FunctionSetAssignments;
import interstore.Types.*;
import interstore.EndDevice.EndDeviceImpl; 
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class FunctionSetAssignmentsService {
    @Autowired
    FunctionSetAssignmentsRepository functionSetAssignmentsRepository;
    
    @Autowired
    private EndDeviceImpl endDeviceImpl;

    private static final Logger LOGGER = Logger.getLogger(FunctionSetAssignmentsService.class.getName());
     
    @Transactional
    public FunctionSetAssignmentsEntity createFunctionsetAssignments(JSONObject payload) {
        LOGGER.info("The payload reached FSA service class: " + payload);
        
        // Create new FSA entity
        FunctionSetAssignmentsEntity fsaEntity = new FunctionSetAssignmentsEntity();
        
        // Save it to the database
        try {
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
            fsaEntity.setFunctionSetAssignmentsLink(Optional.ofNullable(fsaLink));
            LOGGER.info(fsaEntity.getDescription());
            LOGGER.info(fsaEntity.getmRID().toString());
            LOGGER.info(fsaEntity.getSubscribable().toString());
            LOGGER.info(fsaEntity.getVersion().toString());
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
                    LOGGER.info("The key is " + entry.getKey() + " the value is " + jsonValue);
                    if ("functionSetAssignmentsListLink".equals(entry.getKey())) {
                        return jsonValue;
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
        Iterator<String> keys  = payload.keys();
        while(keys.hasNext())
        {
            String key = keys.next();
            switch(key) {
                case "DemandResponseProgramListLink":
                fsaEntity.setDemandResponseProgramListLink(Optional.ofNullable(payload.optString(key)));
                break;
                case "FileListLink":
                fsaEntity.setFileListLink(Optional.ofNullable(payload.optString(key)));
                break;
                case "TraiffProfileListLink":
                fsaEntity.setTariffProfileListLink(Optional.ofNullable(payload.optString(key)));
                break;
                case "MessagingProgramListLink":
                fsaEntity.setMessagingProgramListLink(Optional.ofNullable(payload.optString(key)));
                break;
                case "UsagePointListLink":
                fsaEntity.setUsagePointListLink(Optional.ofNullable(payload.optString(key)));
                break;
                case "DERProgramListLink":
                fsaEntity.setDERProgramListLink(Optional.ofNullable(payload.optString(key)));
                LOGGER.info(fsaEntity.getDERProgramListLink().toString()); 
                break;
                case "CustomerAccountListLink":
                fsaEntity.setCustomerAccountListLink(Optional.ofNullable(payload.optString(key)));
                break;
                case "PrepaymentListLink":
                fsaEntity.setPrepaymentListLink(Optional.ofNullable(payload.optString(key)));
                break;
                case "ResponseSetListLink":
                fsaEntity.setResponseSetListLink(Optional.ofNullable(payload.optString(key)));
                 default:
                    break;    
    }
    
}
       
    }
   
        public void AddSubscribabaleFsa(Short shortSubscribable,FunctionSetAssignmentsEntity fsaEntity )
        {
          if(shortSubscribable!= 0)
          {
            fsaEntity.setFSASubscribableList(fsaEntity);
          }
        }


       /*get all  function set assignments for an end device   */
    public ResponseEntity<Map<String, Object>> getAllFunctionsetAssignments(Long endDeviceId)
    {   
         
        try {
            //Long EndDeviceId = Long.parseLong(endDeviceId);
             Map<String, Object> responseMap = new HashMap<>();
            List<FunctionSetAssignmentsEntity> fsaEntityList = functionSetAssignmentsRepository.findByEndDeviceId(endDeviceId);
            LOGGER.log(Level.INFO, " fsaEntity retrieved successfully" + fsaEntityList);
            if(fsaEntityList.isEmpty()) {
                responseMap.put("message", "No functionSetAssignments found.");
            }
            else {
                responseMap.put("functionSetAssignments", fsaEntityList);
            }
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving functionSetAssignments", e);

            return ResponseEntity.status(404).body(null);
        }

    }

     /*get a single function set assignments for an end device   */
    public ResponseEntity<Map<String, Object>> getFunctionsetAssignments(Long endDeviceId, Long fsaId)

    {
        try {
            //Long EndDeviceId = Long.parseLong(endDeviceId);
            //Long fsaID = Long.parseLong(fsaId);
            Optional<FunctionSetAssignmentsEntity> fsaEntity = functionSetAssignmentsRepository.findFirstByEndDeviceIdAndId( endDeviceId , fsaId );
            Map<String, Object> result = new HashMap<>();
            if (fsaEntity == null) {
                result.put("message", "No functionsetassignment found for EndDevice ID " +  endDeviceId  + " and FSA ID " +   fsaId );
            } else {
                result.put("RegisteredEndDevice", fsaEntity.get());
            }
            return ResponseEntity.ok(result);
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

SubscribableList object (SubscribableResource)
A List to which a Subscription can be requested.
all attribute (UInt32) «XSDattribute»
The number specifying “all” of the items in the list. Required on GET, ignored otherwise.
results attribute (UInt32) «XSDattribute»
Indicates the number of items in this page of results.




public void createdFSA(FunctionSetAssignmentsList fsaList){
        FunctionSetAssignmentsEntity functionSetAssignment = new FunctionSetAssignmentsEntity(fsaList);
        setFSA(functionSetAssignment);
        functionSetAssignmentsRepository.save(functionSetAssignment);
        fsaList.addFsa(functionSetAssignment);
        Long id = functionSetAssignment.getId();
        String fsaLink = fsaList.getFsaListLink()+"/"+id.toString();
        functionSetAssignment.setFsalink(fsaLink);
        String derpListLink = fsaLink+"/derp";
        functionSetAssignment.setDERProgramListLink(derpListLink);

        DERPList derpList = new DERPList(derpListLink);
        derpList = derpListRepository.save(derpList);
        generateDERP(derpList);
    }

    public void setFSA(FunctionSetAssignmentsEntity functionSetAssignment){
        functionSetAssignment.setDescription("FSA description");
        functionSetAssignment.setmRID("A1000000");
        functionSetAssignment.setSubscribable("1");
        functionSetAssignment.setVersion("3");
    }


 * public void generateDERP(DERPList derpList){
        for(int i = 0; i < 3; i++) {
            DERProgramImpl derProgramImpl = ApplicationContextProvider.getApplicationContext().getBean(DERProgramImpl.class);
            derProgramImpl.createDERP(derpList);
        }
    }
    @Transactional
    public Object getFSA(String Ids) {
        JSONObject val = new JSONObject();
        Map<Long, Object> values = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(Ids);

            for (int i = 0; i < jsonArray.length(); i++) {
                Long id = jsonArray.getLong(i);
                Optional<FunctionSetAssignments> functionSetAssignments = functionSetAssignmentsRepository.findById(id);
                values.put(functionSetAssignments.get().getId(), functionSetAssignments.get().getAll());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return values.toString();
    }

//    public Map<String, String> getderpMap() {
//        FunctionSetAssignments functionSetAssignments = new FunctionSetAssignments();
//        return functionSetAssignments.getDerpMap();
//    }
 * 
 * 
 * 
 *    @Autowired
    DERPListRepository derpListRepository;
 * 
 * 
 * 
 *  }
        List<String> list_Links = new ArrayList<>();
        list_Links.add("DemandResponseProgramListLink");
        list_Links.add("FileListLink");
        list_Links.add("TraiffProfileListLink");
        list_Links.add("MessagingProgramListLink");
        list_Links.add("UsagePointListLink");
        list_Links.add("DERProgramListLink");
        list_Links.add("CustomerAccountListLink");
        list_Links.add("PrepaymentListLink");
        list_Links.add("ResponseSetListLink");
        for(String link : list_Links)
        {
            String value = payload.optString(link);
            listLink.put(link, value);
        }
 * 
 * 
 * 
 */