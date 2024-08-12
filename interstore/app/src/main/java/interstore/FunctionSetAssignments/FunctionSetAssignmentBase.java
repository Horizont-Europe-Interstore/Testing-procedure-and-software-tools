package interstore.FunctionSetAssignments;
import java.util.Optional; 


public interface FunctionSetAssignmentBase {
   // Optional<Resource> getHref();
    //void setHref(Optional<Resource> href);
    
   // Optional<String> getTimeLink();
   // void setTimeLink(Optional<String> timeLink);

    Optional<String>getFunctionSetAssignmentsLink();
    void setFunctionSetAssignmentsLink(Optional<String> resource); 

    Optional<String> getDemandResponseProgramListLink();
    void setDemandResponseProgramListLink(Optional<String> resource);

    Optional<String> getTariffProfileListLink();
    void setTariffProfileListLink(Optional<String> resource);

    Optional<String> getMessagingProgramListLink();
    void setMessagingProgramListLink(Optional<String> resource);

    Optional<String> getFileListLink();
    void setFileListLink(Optional<String> resource);

    Optional<String> getUsagePointListLink();
    void setUsagePointListLink(Optional<String> resource);

    Optional<String> getDERProgramListLink();
    void setDERProgramListLink(Optional<String> resource);

    Optional<String> getCustomerAccountListLink();
    void setCustomerAccountListLink(Optional<String> resource);

    Optional<String> getPrepaymentListLink();
    void setPrepaymentListLink(Optional<String> resource);

    Optional<String> getResponseSetListLink();
    void setResponseSetListLink(Optional<String> resource);
}       











   


