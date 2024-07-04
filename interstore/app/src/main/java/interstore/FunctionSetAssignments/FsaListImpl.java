package interstore.FunctionSetAssignments;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class FsaListImpl {
    private static final Logger LOGGER = Logger.getLogger(FsaListImpl.class.getName());

    @Autowired
    FunctionSetAssignmentsListRepository functionSetAssignmentsListRepository;

    @Transactional
    public Object getFSAList(String fsaListLink) {
        FunctionSetAssignmentsList functionSetAssignmentsList = functionSetAssignmentsListRepository.findByFsaListLink(fsaListLink);
        if (functionSetAssignmentsList == null) {
            throw new EntityNotFoundException("FunctionSetAssignmentsList not found for link: " + fsaListLink);
        }
        List<FunctionSetAssignments> functionSetAssignments = functionSetAssignmentsList.getFsa();
        List<Long> ids = new ArrayList<>();
        for(int i=0; i<functionSetAssignments.size(); i++){
            ids.add(functionSetAssignments.get(i).getId());
        }

        return ids;
    }
}

