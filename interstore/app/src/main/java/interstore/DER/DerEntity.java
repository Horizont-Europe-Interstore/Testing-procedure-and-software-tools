package interstore.DER;

import jakarta.persistence.*;
import interstore.DER.DERAvailabilty.DERAvailabilityEntity;
import interstore.DER.DERCapability.DERCapabilityEntity;
import interstore.DER.DERSettings.DERSettingsEntity;
import interstore.DER.DERStatus.DERStatusEntity;
import interstore.EndDevice.EndDeviceEntity;
import interstore.Identity.SubscribableResourceEntity;
@Entity
public class DerEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "end_device_id", nullable = false)  
    private EndDeviceEntity endDevice;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "subscribabale_Resource") 
    private SubscribableResourceEntity subscribableResourceList;
    
    @Column(name="der_link")
    private String derLink; 

    @Column(name = "der_capability_link")
    private String derCapabilityLink;

    @Column(name = "der_settings_link")
    private String derSettingsLink;

    @Column(name = "der_status_link")
    private String derStatusLink;

    @Column(name = "der_availability_link")
    private String derAvailabilityLink;

    @Column(name = "associated_usage_point_link")
    private String associatedUsagePointLink;

    @Column(name= "associated_der_program_list_link")
    private String associatedDERProgramListLink;

    @Column(name = "current_der_program_link")
    private String currentDERProgramLink;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_capability") 
    private DERCapabilityEntity derCapability;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_availability") 
    private DERAvailabilityEntity derAvailability;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_settings") 
    private DERSettingsEntity derSettings;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_status") 
    private DERStatusEntity derStatus;


     
    /* impliment the DER type later
     * @Enumerated(EnumType.STRING)
      @Column(name = "type")
      private DERType type;
      */
   
    public DerEntity (){
    }

    public Long getId() {
        return id;
    }

    public EndDeviceEntity getEndDevice() {
        return endDevice;
    }

    public void setEndDevice(EndDeviceEntity endDevice) {
        this.endDevice = endDevice;
    } 
    
    public void setDerLink(String derLink) {
        this.derLink = derLink;
    }

    public String getDerLink() {
        return derLink;
    }

    public void setDerCapabilityLink(String derCapabilityLink) {
            
            this.derCapabilityLink = derCapabilityLink;
    }
    
    public String getDerCapabilityLink() {
        return derCapabilityLink;
    }
   // no need of resource use derlink from end devcie . 
    public void setDerSettingsLink(String derSettingsLink) {
        this.derSettingsLink = derSettingsLink ;  
    }

    public String getDerSettingsLink() {
        return derSettingsLink;
    }


    public void setDerStatusLink(String derStatusLink) {
        this.derStatusLink = derStatusLink;
        
    }

    public String getDerStatusLink() {
        return derStatusLink;
    }

    public void setDerAvailabilityLink(String derAvailabilityLink) {
        this.derAvailabilityLink = derAvailabilityLink;
    }
    
    
    public String getDerAvailabilityLink() {
        return derAvailabilityLink;
    }

    public void setAssociatedUsagePointLink(String associatedUsagePointLink)
    {
        this.associatedUsagePointLink = associatedUsagePointLink;
    }

    public String getAssociatedUsagePointLink() {
        return associatedUsagePointLink;
    }

    public void setAssociatedDERProgramListLink(String associatedDERProgramListLink)
    {
        this.associatedDERProgramListLink = associatedDERProgramListLink;
    }

    public String getAssociatedDERProgramListLink() {
        return associatedDERProgramListLink;
    }

    public void setCurrentDERProgramLink(String currentDERProgramLink)
    {
        this.currentDERProgramLink = currentDERProgramLink;
    }

    public String getCurrentDERProgramLink() {
        return currentDERProgramLink;
    }

    public SubscribableResourceEntity getSubscribableResourceList() {
        return subscribableResourceList;
    }

    public void setSubscribableResourceList(SubscribableResourceEntity subscribableResourceList) {
        this.subscribableResourceList = subscribableResourceList;
    }

    public DERCapabilityEntity getDerCapability() {
        return derCapability;
    }

    public void setDerCapability(DERCapabilityEntity derCapability) {
        this.derCapability = derCapability;
    }

    public DERAvailabilityEntity getDerAvailability() {
        return derAvailability;
    }

    public void setDerAvailability(DERAvailabilityEntity derAvailability) {
        this.derAvailability = derAvailability;
    }

    public DERSettingsEntity getDerSettings() {
        return derSettings;
    }

    public void setDerSettings(DERSettingsEntity derSettings) {
        this.derSettings = derSettings;
    }

    public DERStatusEntity getDerStatus() {
        return derStatus;
    }

    public void setDerStatus(DERStatusEntity derStatus) {
        this.derStatus = derStatus;
    }
    

    /* Here starts the getters and setters for DER Settings  */

    
   
    /* Here starts the DER Avaialblity getters and setters */
    
     /* Here starts the getters and setters for DER Availability  */

    
    /* Here starts the getters and setters for DER Status  */


}
