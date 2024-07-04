package interstore.FunctionSetAssignments;

import interstore.ApplicationContextProvider;
import interstore.DERProgram.DERPList;
import interstore.DERProgram.DERPListRepository;
import interstore.DERProgram.DERProgramImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class FsaImpl {
    @Autowired
    FunctionSetAssignmentsRepository functionSetAssignmentsRepository;

    @Autowired
    DERPListRepository derpListRepository;


    private static final Logger LOGGER = Logger.getLogger(FsaImpl.class.getName());

    public void createFSA(FunctionSetAssignmentsList fsaList){
        FunctionSetAssignments functionSetAssignment = new FunctionSetAssignments(fsaList);
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

    public void setFSA(FunctionSetAssignments functionSetAssignment){
        functionSetAssignment.setDescription("FSA description");
        functionSetAssignment.setmRID("A1000000");
        functionSetAssignment.setSubscribable("1");
        functionSetAssignment.setVersion("3");
    }

    public void generateDERP(DERPList derpList){
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
}
