package interstore.FunctionSetAssignments;
import interstore.EndDevice.EndDeviceDto;
import jakarta.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "function_set_assignments")
public class FunctionSetAssignmentsEntity implements FunctionSetAssignmentBase{  

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; 

    @ManyToOne
    private EndDeviceDto endDevice;

    @Column(name = "description")
    private String description;

    @Column(name = "m_RID")
    private String mRID;

    @Column(name = "version")
    private String version;

    @Column(name = "subscribable")
    private String subscribable;
   

    @Column(name = "demand_response_program_list_link")
    private String demandResponseProgramListLink;

    @Column(name = "tariff_profile_list_link")
    private String tariffProfileListLink;

    @Column(name = "messaging_program_list_link")
    private String messagingProgramListLink;

    @Column(name = "file_list_link")
    private String fileListLink;

    @Column(name = "usage_point_list_link")
    private String usagePointListLink;

    @Column(name = "der_program_list_link")
    private String derProgramListLink;

    @Column(name = "customer_account_list_link")
    private String customerAccountListLink;

    @Column(name = "prepayment_list_link")
    private String prepaymentListLink;

    @Column(name = "response_set_list_link")
    private String responseSetListLink;
   
    @Column(name = "function_set_assignments_link")
    private String functionSetAssignmentsLink;

    public FunctionSetAssignmentsEntity(){
        
    }

    


    public Long getId() {
        return id;
    }

    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getmRID() {
        return mRID;
    }

    public void setmRID(String mRID) {
        this.mRID = mRID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubscribable() {
        return subscribable;
    }

    public void setSubscribable(String subscribable) {
        this.subscribable = subscribable;
    }

    @Override
    public Optional<String> getFunctionSetAssignmentsLink() {
        return Optional.ofNullable(functionSetAssignmentsLink);
    }
    @Override
    public void setFunctionSetAssignmentsLink(Optional<String> functionSetAssignmentsLink) {
        this.functionSetAssignmentsLink = functionSetAssignmentsLink.orElse(null);
    }


    @Override
    public Optional<String> getDemandResponseProgramListLink() {
        return Optional.ofNullable(demandResponseProgramListLink);
    }

    @Override
    public void setDemandResponseProgramListLink(Optional<String> demandResponseProgramListLink) {
        this.demandResponseProgramListLink = demandResponseProgramListLink.orElse(null);
    }

    @Override
    public Optional<String> getTariffProfileListLink() {
        return Optional.ofNullable(tariffProfileListLink);
    }

    @Override
    public void setTariffProfileListLink(Optional<String> tariffProfileListLink) {
        this.tariffProfileListLink = tariffProfileListLink.orElse(null);
    }

    @Override
    public Optional<String> getMessagingProgramListLink() {
        return Optional.ofNullable(messagingProgramListLink);
    }

    @Override
    public void setMessagingProgramListLink(Optional<String> messagingProgramListLink) {
        this.messagingProgramListLink = messagingProgramListLink.orElse(null);
    }

    @Override
    public Optional<String> getFileListLink() {
        return Optional.ofNullable(fileListLink);
    }

    @Override
    public void setFileListLink(Optional<String> fileListLink) {
        this.fileListLink = fileListLink.orElse(null);
    }

    @Override
    public Optional<String> getUsagePointListLink() {
        return Optional.ofNullable(usagePointListLink);
    }

    @Override
    public void setUsagePointListLink(Optional<String> usagePointListLink) {
        this.usagePointListLink = usagePointListLink.orElse(null);
    }

    @Override
    public Optional<String> getDERProgramListLink() {
        return Optional.ofNullable(derProgramListLink);
    }

    @Override
    public void setDERProgramListLink(Optional<String> derProgramListLink) {
        this.derProgramListLink = derProgramListLink.orElse(null);
    }

    @Override
    public Optional<String> getCustomerAccountListLink() {
        return Optional.ofNullable(customerAccountListLink);
    }

    @Override
    public void setCustomerAccountListLink(Optional<String> customerAccountListLink) {
        this.customerAccountListLink = customerAccountListLink.orElse(null);
    }

    @Override
    public Optional<String> getPrepaymentListLink() {
        return Optional.ofNullable(prepaymentListLink);
    }

    @Override
    public void setPrepaymentListLink(Optional<String> prepaymentListLink) {
        this.prepaymentListLink = prepaymentListLink.orElse(null);
    }

    @Override
    public Optional<String> getResponseSetListLink() {
        return Optional.ofNullable(responseSetListLink);
    }

    @Override
    public void setResponseSetListLink(Optional<String> responseSetListLink) {
        this.responseSetListLink = responseSetListLink.orElse(null);
    }
  

















 
    
}


/*
 *  @Override
    public Optional<String> getTimeLink() {
        return Optional.ofNullable(timeLink);
    }

    @Override
    public void setTimeLink(Optional<String> timeLink) {
        this.timeLink = timeLink.orElse(null);
    }
 * 
 * @Column(name = "href")
    private String href;

    @Column(name = "time_link")
    private Optional<String> timeLink;

    public FunctionSetAssignmentsList getFsaList() {
        return fsaList;
    }

    public void setFsaList(FunctionSetAssignmentsList fsaList) {
        this.fsaList = fsaList;
    }

    public String getFsalink() {
        return fsalink;
    }

    public void setFsalink(String fsalink) {
        this.fsalink = fsalink;
    }
 * 
 *  public FunctionSetAssignments(FunctionSetAssignmentsList fsaList){
        this.fsaList = fsaList;
    }

      
   private ListLink link;
   private Link theLink;  /file , /cfg , /dr , /upt 
   private static Map<String, List<Object>> fsaDetailsMap = new HashMap<>();

   private static Map<String, String> derpMap = new HashMap<>();

        Map<String, String> values = new HashMap<>();
    public FunctionSetAssignments(String href){
       super(href);
   }

private static final Logger LOGGER = Logger.getLogger(FunctionSetAssignments.class.getName());

   public Map<String, Object> getAll(){
        Map<String, Object> attributes = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                attributes.put(field.getName(), field.get(this));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return attributes;
    }


 */