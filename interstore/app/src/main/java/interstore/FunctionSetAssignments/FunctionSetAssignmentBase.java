package interstore.FunctionSetAssignments;

import java.util.List;
import java.util.Optional;

import interstore.Types.SubscribableType;
import interstore.Types.VersionType;
import interstore.Types.mRIDType;

public interface FunctionSetAssignmentBase {
   
  

    // Function Set Assignments Link getters and setters
    Optional<String> getFunctionSetAssignmentsLink();
    void setFunctionSetAssignmentsLink(Optional<String> resource);

    // Demand Response Program List Link getters and setters
    Optional<String> getDemandResponseProgramListLink();
    void setDemandResponseProgramListLink(Optional<String> resource);

    // Tariff Profile List Link getters and setters
    Optional<String> getTariffProfileListLink();
    void setTariffProfileListLink(Optional<String> resource);

    // Messaging Program List Link getters and setters
    Optional<String> getMessagingProgramListLink();
    void setMessagingProgramListLink(Optional<String> resource);

    // File List Link getters and setters
    Optional<String> getFileListLink();
    void setFileListLink(Optional<String> resource);

    // Usage Point List Link getters and setters
    Optional<String> getUsagePointListLink();
    void setUsagePointListLink(Optional<String> resource);

    // DER Program List Link getters and setters
    Optional<String> getDERProgramListLink();
    void setDERProgramListLink(Optional<String> resource);

    // Customer Account List Link getters and setters
    Optional<String> getCustomerAccountListLink();
    void setCustomerAccountListLink(Optional<String> resource);

    // Prepayment List Link getters and setters
    Optional<String> getPrepaymentListLink();
    void setPrepaymentListLink(Optional<String> resource);

    // Response Set List Link getters and setters
    Optional<String> getResponseSetListLink();
    void setResponseSetListLink(Optional<String> resource);

    // Additional getters and setters from the entity class

    // Description getters and setters
    String getDescription();
    void setDescription(String description);

    // mRIDType getters and setters
    String getmRID();
    void setmRID(String mRID);

    // VersionType getters and setters
    Integer getVersion();
    void setVersion(Integer version);

      // SubscribableType getters and setters
      Short getSubscribable();
      void setSubscribable(Short subscribable);

    List<FunctionSetAssignmentsEntity> getFSASubscribableList();
    void setFSASubscribableList(FunctionSetAssignmentsEntity functionSetAssignmentSubscribableList);
}









   


