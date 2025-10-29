package interstore.EndDevice;
import com.fasterxml.jackson.annotation.JsonIgnore;
import interstore.AbstractDevice;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsEntity;
import jakarta.persistence.*;
import java.util.List;
import java.util.logging.Logger;


@Entity
public class EndDeviceDto implements AbstractDevice{ 
     private static final Logger LOGGER = Logger.getLogger(EndDeviceDto.class.getName()); 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "device_category")
    private String deviceCategory;

    @Column(name = "lfdi")
    private String lFDI;

    @Column(name = "sfdi")
     private Long sFDI; 
    
    @Column(name = "link_fsa")
    private String linkFsa;

    @Column(name = "link_rg")
    private String linkRg;

    @Column(name = "link_sub")
    private String linkSub;

    @Column(name = "link_dstat")
    private String linkDstat;
 
    @Column(name= "link_enddevice")
    private String linkEndDevice;

    @Column(name= "link_der_list")
    private String linkDerList;
    
    @Column(name= "configuration_link")
    private String configurationLink;

    @Column(name= "device_information_link")
    private String deviceInformationLink;

    @Column(name= "file_status_link")
    private String fileStatusLink;

    @Column(name= "power_status_link")
    private String powerStatusLink;



    public EndDeviceDto() {
        
    } 
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    

    public void setsfdi(long sfdi)

   {   
      this.sFDI = sfdi;
  
   
   }
  
    public Long getsfdi()
   {
      return this.sFDI;
    
   }
   
   public void setEndDeviceLink(String link)
   {
      this.linkEndDevice = link;

   }

    public String getEndDeviceLink()
   {
      return this.linkEndDevice;

   } 

  
    @Override
    public void setDeviceCategory(String deviceCategory) {
        
        this.deviceCategory = deviceCategory;
       
    }
    @Override
    public String getDeviceCategory()
    {
       return this.deviceCategory; 
        
    }
    @Override
    public void sethexBinary160(String lfdi) {
      
        this.lFDI = lfdi;
      
    }
    @Override
    public String gethexBinary160()
    {  
        return this.lFDI; 
        
    } 
    @Override
    public void setFunctionSetAssignmentsListLink(String link) {
         
        this.linkFsa = link;
    }

    
     @Override 
    public String getFunctionSetAssignmentsListLink() 
    {   
        return this.linkFsa;
        
    
       
    }
    @Override
    public void setSubscriptionListLink(String link) {
        this.linkSub = link;
    
  
    }
    @Override
    public String getSubscriptionListLink()
    {  
        return this.linkSub;
        
    }
    @Override
    public void setDeviceStatusLink(String link) {
        this.linkDstat = link;
       
    
    

    }
    @Override
    public String getDeviceStatusLink()
    {
        return this.linkDstat; 
        
    }
    @Override
    public void setRegistrationLink(String link) {
        this.linkRg = link;
       

    }
    @Override 
    public String getRegistrationLink()
    {
        return this.linkRg; 
        
    }
    
    @Override
    public void setDERListLink(String link)
    {
        this.linkDerList = link;
    }
    @Override
    public String getDERListLink()
    {
        return this.linkDerList;
    }
    
    @Override
    public void setFileStatusLink(String link)
    {
        this.fileStatusLink = link;
    }
    @Override
    public String getFileStatusLink() 
    {
        return this.fileStatusLink; 
    } 
    @Override
    public void setDeviceInformationLink(String link)
    {
        this.deviceInformationLink = link;
    }

    @Override
    public String getDeviceInformationLink() 
    {
        return this.deviceInformationLink; 
    }   
    @Override
    public void setConfigurationLink(String link)
    {
        this.configurationLink = link;
    }


    @Override
    public String getConfigurationLink() 
    {
        return this.configurationLink; 
     }



    @JsonIgnore 
    @Override
    public void setPowerStatusLink(String link)
    {
        this.powerStatusLink = link;
    }

    @Override
    @JsonIgnore
    public String getPowerStatusLink() {
        return this.powerStatusLink; 
    }

    @Override
    @JsonIgnore
    public void setIPInterfaceListLink(String link)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    @JsonIgnore
    public  String getIPInterfaceListLink()
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    @JsonIgnore
    public void setLoadShedAvailabilityListLink(String link)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @JsonIgnore
    public String getLoadShedAvailabilityListLink()
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    @Override
    @JsonIgnore
    public void setLogEventListLink(String link)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @JsonIgnore
    public String getLogEventListLink()
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }


} 


/*
     @OneToMany(mappedBy = "endDevice", cascade = CascadeType.ALL)
    private List<FunctionSetAssignmentsEntity> functionSetAssignments;

      public List<FunctionSetAssignmentsEntity> setFunctionSetAssignments(List<FunctionSetAssignmentsEntity> functionSetAssignments)
  {
      return this.functionSetAssignments = functionSetAssignments;
  }
  
  public List<FunctionSetAssignmentsEntity> getFunctionSetAssignments()
  {
      return this.functionSetAssignments;
  }

 */



 