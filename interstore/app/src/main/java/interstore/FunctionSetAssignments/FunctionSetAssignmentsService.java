package interstore.FunctionSetAssignments;
import interstore.Identity.*;
import interstore.Types.*;
import interstore.ApplicationContextProvider;
import interstore.DERProgram.DERPList;
import interstore.DERProgram.DERPListRepository;
import interstore.DERProgram.DERProgramImpl;
import interstore.EndDevice.EndDeviceDto;
import interstore.EndDevice.EndDeviceRepository;
import interstore.EndDevice.EndDeviceImpl; 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
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
    private static final Logger LOGGER = Logger.getLogger(FunctionSetAssignmentsService.class.getName());

    @Transactional
    public FunctionSetAssignmentsEntity createFunctionsetAssignments(JSONObject  payload)
    {
        FunctionSetAssignmentsEntity fsaEntity = new FunctionSetAssignmentsEntity();
        fsaEntity = functionSetAssignmentsRepository.save(fsaEntity);
        LOGGER.log(Level.INFO, "EndDeviceDto saved successfully" +    fsaEntity );
        setFunctionSetAssignmentEntity(fsaEntity, payload);
        return fsaEntity ;
       
    }
     /*
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
         fsa uri . so i can create the fsa link 
         "functionSetAssignmentsListLink":"http://localhost/edev/1/fsa"

      */
    public void setFunctionSetAssignmentEntity(FunctionSetAssignmentsEntity fsaEntity , JSONObject payload)
      {
            Long fsaId = fsaEntity.getId();
            JSONObject Fsapayload = payload.optJSONObject("payload");
            String endDeviceId = Fsapayload.optString("EndDeviceID");
            Long endDeviceIdLong = Long.parseLong(endDeviceId); 
            String idString = "/"+ String.valueOf(fsaId) ;
            String functionsetassignmentLink = createFunctionSetAssignmentsLink(endDeviceIdLong );
            String fsaLink = functionsetassignmentLink + idString;
            fsaEntity.setFunctionSetAssignmentsLink(Optional.ofNullable(fsaLink));

            mRIDType mRid = new mRIDType( Fsapayload.optString("mRID"));
            String description = Fsapayload.optString("description");
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
            findListLink(Fsapayload, fsaEntity);
           

      }
     
      public String createFunctionSetAssignmentsLink(Long id )
      {
        EndDeviceImpl endDeviceImpl = new EndDeviceImpl();
        ResponseEntity<Map<String, Object>> responseEntity = endDeviceImpl.getEndDevice(id);
        Map<String, Object> endDeviceMap = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        for(Map.Entry<String, Object> entry : endDeviceMap.entrySet())
        {
            try {
                String jsonValue = objectMapper.writeValueAsString(entry.getValue()); 
                LOGGER.info("The key is " + entry.getKey() + " the value is " + jsonValue);
                if(entry.getKey() == "functionSetAssignmentsListLink")
                {
                    return jsonValue;
                }
            } catch (JsonProcessingException e) {
                LOGGER.info("error occured while converting to json" + e.getMessage());
            }   
          
        }
        return null; 
      }
    
    public  void findListLink(JSONObject payload, FunctionSetAssignmentsEntity fsaEntity)
    {
        @SuppressWarnings("unchecked")
        Iterator<String> keys  = payload.keys();
        while(keys.hasNext())
        {
            String key = keys.next();
            switch(key) {
                case "DemandResponseProgramListLink":
                fsaEntity.setDERProgramListLink(Optional.ofNullable(payload.optString(key)));
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


}







/*

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