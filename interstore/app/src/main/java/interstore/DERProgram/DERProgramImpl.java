package interstore.DERProgram;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsService;
import interstore.Identity.*;
import jakarta.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class DERProgramImpl {
    private static final Logger LOGGER = Logger.getLogger(DERProgramImpl.class.getName());

    @Autowired
    DERProgramRepository derProgramRepository;

    @Autowired
    SubscribableIdentifiedObjectRepository subscribableIdentifiedObjectRepository;
    
    @Autowired
    SubscribableResourceEntity subscribableResourceEntity;
   

      /* here i expect a json string here 
       *  payload = {
       *  "derpListLink": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
       *   ActiveDERControlListLink .subscribable is short 
       *}
       * 
       *  {"payload" : { "subscribable" : "", "mRID": "", 
       *  "description": "", "version": "", "activeDERControlListLink": "", "defaultDERControlLink": ""
       *  "dERControlListLink": "", "dERCurveListLink": "", " primacy" : "" ,  "derpLink" : "" 
       * }}
       * 
       */
    @Transactional
    public DERProgramEntity createDERP(JSONObject payload )  throws NumberFormatException, JSONException, NotFoundException{
         JSONObject DerProgrampayload = payload.optJSONObject("payload");
         short subscribable = (short) DerProgrampayload.optInt("subscribable");
         String mRID = DerProgrampayload.optString("mRID");
         String description = DerProgrampayload.optString("description");
         String version = DerProgrampayload.optString("version");
         

        setDERP(derProgram);
        derProgram = derProgramRepository.save(derProgram);
        return derProgram;
    }


    public void setDERP(DERProgramEntity derProgram){
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
}



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