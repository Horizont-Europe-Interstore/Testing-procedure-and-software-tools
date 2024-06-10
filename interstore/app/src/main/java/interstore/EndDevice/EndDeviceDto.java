package interstore.EndDevice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import interstore.AbstractDevice;
import jakarta.persistence.*;

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



    @JsonIgnore 
    @Override
    public void setPowerStatusLink(String link)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @JsonIgnore
    public String getPowerStatusLink() {
        throw new UnsupportedOperationException("Not supported yet."); 
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
    
    @Override
    @JsonIgnore
    public void setFileStatusLink(String link)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    @JsonIgnore
    public String getFileStatusLink() 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    } 
    @Override
    @JsonIgnore
    public void setDeviceInformationLink(String link)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @JsonIgnore
    public String getDeviceInformationLink() 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }   
    @Override
    @JsonIgnore
    public void setConfigurationLink(String link)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    @JsonIgnore
    public String getConfigurationLink() 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
     }
   



} 


/*

   
    // Assuming you want to store a list of links in a separate table
   // @ElementCollection
    //@CollectionTable(name = "end_device_links")
   // private Set<String> endDeviceLinks = new HashSet<>();
 * public Set<String> getLinks() {
        return endDeviceLinks;
    }

    public void setLinks(Set<String> links) {
        this.endDeviceLinks = links;
    }

    public void addLink(String link) {
        this.endDeviceLinks.add(link);
    }
    
  import java.util.Set;
  import java.util.HashSet; 
 
  import interstore.Identity.Link;
import interstore.Identity.ListLink;
import interstore.Types.DeviceCategoryType;
import interstore.Types.HexBinary160;
import interstore.Types.SFDIType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
 */



 