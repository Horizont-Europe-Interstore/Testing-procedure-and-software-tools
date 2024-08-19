package interstore.FunctionSetAssignments;
import interstore.EndDevice.EndDeviceDto;
import interstore.Identity.Resource;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "function_set_assignments")
public class FunctionSetAssignmentsEntity implements FunctionSetAssignmentBase{  

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; 

    @ManyToOne
    @JoinColumn(name = "end_device_id")  // This column will store the foreign key to EndDeviceDto
    private EndDeviceDto endDevice;

    @Column(name = "description")
    private String description;

    @Column(name = "m_rid")
    private String mRID; // HexBinary128 is now a String

    @Column(name = "version")
    private Integer version; // UInt16 is now an Integer

    @Column(name = "subscribable")
    private Short subscribable;
   

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    private List<FunctionSetAssignmentsEntity> functionSetAssignmentSubscribableList = new ArrayList<>();



    public FunctionSetAssignmentsEntity(){
        
    }

    

    public Long getId() {
        return id;
    }

    public EndDeviceDto getEndDevice() {
        return endDevice;
    }

    public void setEndDevice(EndDeviceDto endDevice) {
        this.endDevice = endDevice;
    } 

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
   
    @Override
    public String getmRID() {
        return mRID;
    }
    @Override
    public void setmRID(String mRID) {
        this.mRID = mRID;
    }
    @Override
    public Integer getVersion() {
        return version;
    }
    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }
    @Override
    public Short getSubscribable() {
        return subscribable;
    }
    @Override
    public void setSubscribable(Short subscribable) {
        this.subscribable = subscribable;
    }

    @Override
    public List<FunctionSetAssignmentsEntity> getFSASubscribableList() {
        return functionSetAssignmentSubscribableList;
    }

    @Override
    public void setFSASubscribableList(FunctionSetAssignmentsEntity functionSetAssignmentSubscribableList) {
        this.functionSetAssignmentSubscribableList.add(functionSetAssignmentSubscribableList);
        
    }


    @Override
    public String getFunctionSetAssignmentsLink() {
        return functionSetAssignmentsLink;
    }
    @Override
    public void setFunctionSetAssignmentsLink(String functionSetAssignmentsLink) {
        this.functionSetAssignmentsLink = functionSetAssignmentsLink;
    }


    @Override
    public String getDemandResponseProgramListLink() {
        return demandResponseProgramListLink;
    }

    @Override
    public void setDemandResponseProgramListLink(String demandResponseProgramListLink) {
        
            Resource resource = new Resource();
            resource.setHref(demandResponseProgramListLink);
            this.demandResponseProgramListLink = resource.getHref();
    }

    @Override
    public String getTariffProfileListLink() {
        return tariffProfileListLink;
    }

    @Override
    public void setTariffProfileListLink(String tariffProfileListLink) {
        {
            Resource resource = new Resource();
            resource.setHref(tariffProfileListLink);
            this.tariffProfileListLink = resource.getHref();
        }
      
    }

    @Override
    public String getMessagingProgramListLink() {
        return messagingProgramListLink;
    }

    @Override
    public void setMessagingProgramListLink(String messagingProgramListLink) {
     
            Resource resource = new Resource();
            resource.setHref(messagingProgramListLink);
            this.messagingProgramListLink = resource.getHref();
      
        
    }

    @Override
    public String getFileListLink() {
        return fileListLink;
    }

    @Override
    public void setFileListLink(String fileListLink) {
      
            Resource resource = new Resource();
            resource.setHref(fileListLink);
            this.fileListLink = resource.getHref();
       
    }

    @Override
    public String getUsagePointListLink() {
        return usagePointListLink;
    }

    @Override
    public void setUsagePointListLink(String usagePointListLink) {
        
            Resource resource = new Resource();
            resource.setHref(usagePointListLink);
            this.usagePointListLink = resource.getHref();
    }
   

    @Override
    public String getDERProgramListLink() {
        return derProgramListLink;
    }

    @Override
    public void setDERProgramListLink(String derProgramListLink) {
        
            Resource resource = new Resource();
            resource.setHref(derProgramListLink);
            this.derProgramListLink = resource.getHref();
    }

    @Override
    public String getCustomerAccountListLink() {
        return customerAccountListLink;
    }

    @Override
    public void setCustomerAccountListLink(String customerAccountListLink) {
      
            Resource resource = new Resource();
            resource.setHref(customerAccountListLink);
            this.customerAccountListLink = resource.getHref();
    }

    @Override
    public String getPrepaymentListLink() {
        return prepaymentListLink;
    }

    @Override
    public void setPrepaymentListLink(String prepaymentListLink) {
     
            Resource resource = new Resource();
            resource.setHref(prepaymentListLink);
            this.prepaymentListLink = resource.getHref();
      
    }

    @Override
    public String getResponseSetListLink() {
        return responseSetListLink ;
    }

    @Override
    public void setResponseSetListLink( String responseSetListLink) {
            Resource resource = new Resource();
            resource.setHref(responseSetListLink);
            this.responseSetListLink = resource.getHref();
    }
  

    
}


/*


@Column(name = "version")
    private String version;
    

 @Column(name = "subscribable")
    private String subscribable;
   

  public String getmRID() {
        return mRID;
    }

    public void setmRID(String mRID) {
        this.mRID = mRID;
    }




 @Column(name = "m_RID")
    private String mRID;
    
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