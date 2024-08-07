package interstore.DERProgram;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void createDERP(DERPList derpList){
        DERProgram derProgram = new DERProgram(derpList);
        setDERP(derProgram);
        derProgram = derProgramRepository.save(derProgram);
        derpList.addDerpDto(derProgram);
        setDERPLinks(derProgram, derpList);
    }

    public void setDERP(DERProgram derProgram){
        derProgram.setmRID("B01000000");
        derProgram.setPrimacy("89");
        derProgram.setDescription("SUBTX-A1-B1");
        derProgram.setVersion("1");
        derProgram.setSubscribable("0");
    }

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

    public String getDerProgramByListLinks(Object derpListLinks) throws JSONException {
        Map<Long, Object> derProgramMap = new HashMap<>();
        if (derpListLinks instanceof JSONArray) {
            JSONArray linksArray = (JSONArray) derpListLinks;

            for (int i = 0; i < linksArray.length(); i++) {
                String link = linksArray.getString(i);
                List<DERProgram> values = derProgramRepository.findAllByDerpListLink(link);
                for (DERProgram derProgram : values) {
                    derProgramMap.put(derProgram.getId(), derProgram.getAll());
                }
            }
        }
        return derProgramMap.toString();
    }
}
