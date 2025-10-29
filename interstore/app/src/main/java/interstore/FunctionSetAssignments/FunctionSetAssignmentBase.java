package interstore.FunctionSetAssignments;

import java.util.List;
import java.util.Optional;

import interstore.Types.SubscribableType;
import interstore.Types.VersionType;
import interstore.Types.mRIDType;

public interface FunctionSetAssignmentBase {
   
  

    // Function Set Assignments Link getters and setters
  String getFunctionSetAssignmentsLink();
    void setFunctionSetAssignmentsLink(String resource);

    // Demand Response Program List Link getters and setters
    String getDemandResponseProgramListLink();
    void setDemandResponseProgramListLink(String resource);

    // Tariff Profile List Link getters and setters
   String getTariffProfileListLink();
    void setTariffProfileListLink(String resource);

    // Messaging Program List Link getters and setters
    String getMessagingProgramListLink();
    void setMessagingProgramListLink(String resource);

    // File List Link getters and setters
    String getFileListLink();
    void setFileListLink(String resource);

    // Usage Point List Link getters and setters
    String getUsagePointListLink();
    void setUsagePointListLink(String resource);

    // DER Program List Link getters and setters
    String getDERProgramListLink();
    void setDERProgramListLink(String resource);

    // Customer Account List Link getters and setters
    String getCustomerAccountListLink();
    void setCustomerAccountListLink(String resource);

    // Prepayment List Link getters and setters
    String getPrepaymentListLink();
    void setPrepaymentListLink(String resource);

    // Response Set List Link getters and setters
    String getResponseSetListLink();
    void setResponseSetListLink(String resource);

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









   


