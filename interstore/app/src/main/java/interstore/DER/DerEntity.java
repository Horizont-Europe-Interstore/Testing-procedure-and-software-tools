package interstore.DER;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import interstore.EndDevice.EndDeviceDto;
import interstore.Identity.SubscribableResourceEntity;
@Entity
public class DerEntity  {
    private static final Logger LOGGER = Logger.getLogger(DerEntity .class.getName());
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "end_device_id", nullable = false)  
    private EndDeviceDto endDevice;

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

   // Here Starts  DER settings Attributes 
    @Column(name="modesEnabled")
    private Integer modesEnabled;

    @Column(name="setESDelay")
    private Long setESDelay;
   
    @Column(name="setESHighFreq")
    private Long setESHighFreq;

    @Column(name = "setESLowFreq")
    private Long setESLowFreq;

    @Column(name = "setESHighVolt")
    private Long setESHighVolt;

    @Column(name = "setESLowVolt")
    private Long setESLowVolt;

    @Column(name="setESRampTms")
    private Long setESRampTms;

    @Column(name = "setESRandomDelay")
    private Long setESRandomDelay;

    @Column(name="setGradW")
    private Long setGradW;

    @Column(name = "setSoftGradW")
    private Long setSoftGradW; 
    
    
    @Column(name = "setMaxA")
    private Double setMaxA; 
    
    @Column(name = "setMaxChargeRateVA")
    private Double setMaxChargeRateVA;

    @Column(name = "setMaxChargeRateW")
    private Double setMaxChargeRateW;

    @Column(name = "setMaxDischargeRateVA")
    private Double setMaxDischargeRateVA;

    @Column(name = "setMaxDischargeRateW")
    private Double setMaxDischargeRateW;

    @Column(name = "setMaxV")
    private Double setMaxV;

    @Column(name = "setMaxVA")
    private Double setMaxVA;

    @Column(name = "setMaxVar")
    private Double setMaxVar;

    @Column(name = "setMaxVarNeg")
    private Double setMaxVarNeg;

    @Column(name = "setMaxW")
    private Double setMaxW;

    @Column(name = "setMaxWh")
    private Double setMaxWh;

    @Column(name = "setMinPFOverExcited")
    private Double setMinPFOverExcited;

    @Column(name = "setMinPFUnderExcited")
    private Double setMinPFUnderExcited;

    @Column(name = "setMinV")
    private Double setMinV;
    
    @Column(name = "setVNom")
    private Double setVNom;
    
    @Column(name = "setVRef")
    private Double setVRef;

    @Column(name = "setVRefOfs")
    private Double setVRefOfs;

    @Column(name = "updatedTime")
    private String updatedTime;
  
    // Here Starts  DER Capability  Attributes 
    //@ElementCollection
   // @CollectionTable(name = "der_capability_modes_supported", joinColumns = @JoinColumn(name = "der_entity_id", referencedColumnName = "id"))
    //@Column(name = "mode")
   // private List<String> modesSupported = new ArrayList<>();
    
    @Column(name = "modesSupported")
    private String modesSupported;

    @Column(name = "rtgAbnormalCategory")
    private Integer rtgAbnormalCategory; 

    @Column(name = "rtgMaxA")
    private Double rtgMaxA;  

    @Column(name = "rtgMaxAh")
    private Double rtgMaxAh;  

    @Column(name = "rtgMaxChargeRateVA")
    private Double rtgMaxChargeRateVA;  

    @Column(name = "rtgMaxChargeRateW")
    private Double rtgMaxChargeRateW; 

    @Column(name = "rtgMaxDischargeRateVA")
    private Double rtgMaxDischargeRateVA;  

    @Column(name = "rtgMaxDischargeRateW")
    private Double rtgMaxDischargeRateW;  

    @Column(name = "rtgMaxV")
    private Double rtgMaxV;  

    @Column(name = "rtgMaxVA")
    private Double rtgMaxVA;  

    @Column(name = "rtgMaxVar")
    private Double rtgMaxVar;  

    @Column(name = "rtgMaxVarNeg")
    private Double rtgMaxVarNeg;  

    @Column(name = "rtgMaxW")
    private Double rtgMaxW;  

    @Column(name = "rtgMaxWh")
    private Double rtgMaxWh;  

    @Column(name = "rtgMinPFOverExcited")
    private Double rtgMinPFOverExcited;  

    @Column(name = "rtgMinPFUnderExcited")
    private Double rtgMinPFUnderExcited;  

    @Column(name = "rtgMinV")
    private Double rtgMinV;  

    @Column(name = "rtgNormalCategory")
    private Integer rtgNormalCategory;

    @Column(name = "rtgOverExcitedPF")
    private Double rtgOverExcitedPF;

    @Column(name = "rtgOverExcitedW")
    private Double rtgOverExcitedW;

    @Column(name = "rtgReactiveSusceptance")
    private Double rtgReactiveSusceptance;

    @Column(name = "rtgUnderExcitedPF")
    private Double rtgUnderExcitedPF;

    @Column(name = "rtgUnderExcitedW")
    private Double rtgUnderExcitedW;

    @Column(name = "rtgVNom")
    private Double rtgVNom;

    @Column(name = "type")
    private Integer derType;

     // Der Availability attributes 
    @Column(name = "availabilityDuration")
    private Long availabilityDuration;  

    @Column(name = "maxChargeDuration")
    private Long maxChargeDuration;  

    @Column(name = "readingTimeAvailability")
    private String readingTimeAvailability; 

    @Column(name = "reserveChargePercent")
    private Double reserveChargePercent;  

    @Column(name = "reservePercent")
    private Double reservePercent;  

    @Column(name = "statVarAvail")
    private Double statVarAvail;  

    @Column(name = "statWAvail")
    private Double statWAvail;  
    
    // Der status attributes 
    @Column(name = "alarmStatus")
    private String alarmStatus;  

    @Column(name = "genConnectStatus")
    private String genConnectStatus; 

    @Column(name = "inverterStatus")
    private String inverterStatus; 

    @Column(name = "localControlModeStatus")
    private String localControlModeStatus;  

    @Column(name = "manufacturerStatus")
    private String manufacturerStatus;  

    @Column(name = "operationalModeStatus")
    private String operationalModeStatus;  

    @Column(name = "readingTimeStatus")
    private String readingTimeStatus;  

    @Column(name = "stateOfChargeStatus")
    private String stateOfChargeStatus;  

    @Column(name = "storageModeStatus")
    private String storageModeStatus;  

    @Column(name = "storConnectStatus")
    private String storConnectStatus; 

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

    public EndDeviceDto getEndDevice() {
        return endDevice;
    }

    public void setEndDevice(EndDeviceDto endDevice) {
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
    

    public Integer getDerType() {
        return derType;
    }

    public void setDerType(Integer derType) {
        if (!isValidDERType(derType)) {
            throw new IllegalArgumentException("Invalid DERType value: " + derType);
        }
        this.derType = derType;
    }

    // Validation logic
    private boolean isValidDERType(Integer value) {
        return (value >= 0 && value <= 6) || (value >= 80 && value <= 83);
    }

    /* Here starts the getters and setters for DER Settings  */

    public Integer getModesEnabled() {
        return modesEnabled;
    }

    public void setModesEnabled(Integer modesEnabled) {
        this.modesEnabled = modesEnabled;
    }

    public Long getSetESDelay() {
        return setESDelay;
    }

    public void setSetESDelay(Long setESDelay) {
        this.setESDelay = setESDelay;
    }

    public Long getSetESHighFreq() {
        return setESHighFreq;
    }

    public void setSetESHighFreq(Long setESHighFreq) {
        this.setESHighFreq = setESHighFreq;
    }

    public Long getSetESLowFreq() {
        return setESLowFreq;
    }

    public void setSetESLowFreq(Long setESLowFreq) {
        this.setESLowFreq = setESLowFreq;
    }

    public Long getSetESHighVolt() {
        return setESHighVolt;
    }

    public void setSetESHighVolt(Long setESHighVolt) {
        this.setESHighVolt = setESHighVolt;
    }

    public Long getSetESLowVolt() {
        return setESLowVolt;
    }

    public void setSetESLowVolt(Long setESLowVolt) {
        this.setESLowVolt = setESLowVolt;
    }

    public Long getSetESRampTms() {
        return setESRampTms;
    }

    public void setSetESRampTms(Long setESRampTms) {
        this.setESRampTms = setESRampTms;
    }

    public Long getSetESRandomDelay() {
        return setESRandomDelay;
    }

    public void setSetESRandomDelay(Long setESRandomDelay) {
        this.setESRandomDelay = setESRandomDelay;
    }

    public Long getSetGradW() {
        return setGradW;
    }

    public void setSetGradW(Long setGradW) {
        this.setGradW = setGradW;
    }

    public Long getSetSoftGradW() {
        return setSoftGradW;
    }

    public void setSetSoftGradW(Long setSoftGradW) {
        this.setSoftGradW = setSoftGradW;
    }

    public Double getSetMaxA() {
        return setMaxA;
    }

    public void setSetMaxA(Double setMaxA) {
        this.setMaxA = setMaxA;
    }

    public Double getSetMaxChargeRateVA() {
        return setMaxChargeRateVA;
    }

    public void setSetMaxChargeRateVA(Double setMaxChargeRateVA) {
        this.setMaxChargeRateVA = setMaxChargeRateVA;
    }

    public Double getSetMaxChargeRateW() {
        return setMaxChargeRateW;
    }

    public void setSetMaxChargeRateW(Double setMaxChargeRateW) {
        this.setMaxChargeRateW = setMaxChargeRateW;
    }

    public Double getSetMaxDischargeRateVA() {
        return setMaxDischargeRateVA;
    }

    public void setSetMaxDischargeRateVA(Double setMaxDischargeRateVA) {
        this.setMaxDischargeRateVA = setMaxDischargeRateVA;
    }

    public Double getSetMaxDischargeRateW() {
        return setMaxDischargeRateW;
    }

    public void setSetMaxDischargeRateW(Double setMaxDischargeRateW) {
        this.setMaxDischargeRateW = setMaxDischargeRateW;
    }

    public Double getSetMaxV() {
        return setMaxV;
    }

    public void setSetMaxV(Double setMaxV) {
        this.setMaxV = setMaxV;
    }

    public Double getSetMaxVA() {
        return setMaxVA;
    }

    public void setSetMaxVA(Double setMaxVA) {
        this.setMaxVA = setMaxVA;
    }

    public Double getSetMaxVar() {
        return setMaxVar;
    }

    public void setSetMaxVar(Double setMaxVar) {
        this.setMaxVar = setMaxVar;
    }

    public Double getSetMaxVarNeg() {
        return setMaxVarNeg;
    }

    public void setSetMaxVarNeg(Double setMaxVarNeg) {
        this.setMaxVarNeg = setMaxVarNeg;
    }

    public Double getSetMaxW() {
        return setMaxW;
    }

    public void setSetMaxW(Double setMaxW) {
        this.setMaxW = setMaxW;
    }

    public Double getSetMaxWh() {
        return setMaxWh;
    }

    public void setSetMaxWh(Double setMaxWh) {
        this.setMaxWh = setMaxWh;
    }

    public Double getSetMinPFOverExcited() {
        return setMinPFOverExcited;
    }

    public void setSetMinPFOverExcited(Double setMinPFOverExcited) {
        this.setMinPFOverExcited = setMinPFOverExcited;
    }

    public Double getSetMinPFUnderExcited() {
        return setMinPFUnderExcited;
    }

    public void setSetMinPFUnderExcited(Double setMinPFUnderExcited) {
        this.setMinPFUnderExcited = setMinPFUnderExcited;
    }

    public Double getSetMinV() {
        return setMinV;
    }

    public void setSetMinV(Double setMinV) {
        this.setMinV = setMinV;
    }

    public Double getSetVNom() {
        return setVNom;
    }

    public void setSetVNom(Double setVNom) {
        this.setVNom = setVNom;
    }

    public Double getSetVRef() {
        return setVRef;
    }

    public void setSetVRef(Double setVRef) {
        this.setVRef = setVRef;
    }
   
    /* Here starts the DER Avaialblity getters and setters */
    public String getModesSupported() {
        return modesSupported;
    }

    public void setModesSupported(String modesSupported) {
        this.modesSupported = modesSupported;
    }

    public Integer getRtgAbnormalCategory() {
        return rtgAbnormalCategory;
    }

    public void setRtgAbnormalCategory(Integer rtgAbnormalCategory) {
        this.rtgAbnormalCategory = rtgAbnormalCategory;
    }

    public Double getRtgMaxA() {
        return rtgMaxA;
    }

    public void setRtgMaxA(Double rtgMaxA) {
        this.rtgMaxA = rtgMaxA;
    }

    public Double getRtgMaxAh() {
        return rtgMaxAh;
    }

    public void setRtgMaxAh(Double rtgMaxAh) {
        this.rtgMaxAh = rtgMaxAh;
    }

    public Double getRtgMaxChargeRateVA() {
        return rtgMaxChargeRateVA;
    }

    public void setRtgMaxChargeRateVA(Double rtgMaxChargeRateVA) {
        this.rtgMaxChargeRateVA = rtgMaxChargeRateVA;
    }

    public Double getRtgMaxChargeRateW() {
        return rtgMaxChargeRateW;
    }

    public void setRtgMaxChargeRateW(Double rtgMaxChargeRateW) {
        this.rtgMaxChargeRateW = rtgMaxChargeRateW;
    }

    public Double getRtgMaxDischargeRateVA() {
        return rtgMaxDischargeRateVA;
    }

    public void setRtgMaxDischargeRateVA(Double rtgMaxDischargeRateVA) {
        this.rtgMaxDischargeRateVA = rtgMaxDischargeRateVA;
    }

    public Double getRtgMaxDischargeRateW() {
        return rtgMaxDischargeRateW;
    }

    public void setRtgMaxDischargeRateW(Double rtgMaxDischargeRateW) {
        this.rtgMaxDischargeRateW = rtgMaxDischargeRateW;
    }

    public Double getRtgMaxV() {
        return rtgMaxV;
    }

    public void setRtgMaxV(Double rtgMaxV) {
        this.rtgMaxV = rtgMaxV;
    }

    public Double getRtgMaxVA() {
        return rtgMaxVA;
    }

    public void setRtgMaxVA(Double rtgMaxVA) {
        this.rtgMaxVA = rtgMaxVA;
    }

    public Double getRtgMaxVar() {
        return rtgMaxVar;
    }

    public void setRtgMaxVar(Double rtgMaxVar) {
        this.rtgMaxVar = rtgMaxVar;
    }

    public Double getRtgMaxVarNeg() {
        return rtgMaxVarNeg;
    }

    public void setRtgMaxVarNeg(Double rtgMaxVarNeg) {
        this.rtgMaxVarNeg = rtgMaxVarNeg;
    }

    public Double getRtgMaxW() {
        return rtgMaxW;
    }

    public void setRtgMaxW(Double rtgMaxW) {
        this.rtgMaxW = rtgMaxW;
    }

    public Double getRtgMaxWh() {
        return rtgMaxWh;
    }

    public void setRtgMaxWh(Double rtgMaxWh) {
        this.rtgMaxWh = rtgMaxWh;
    }

    public Double getRtgMinPFOverExcited() {
        return rtgMinPFOverExcited;
    }

    public void setRtgMinPFOverExcited(Double rtgMinPFOverExcited) {
        this.rtgMinPFOverExcited = rtgMinPFOverExcited;
    }

    public Double getRtgMinPFUnderExcited() {
        return rtgMinPFUnderExcited;
    }

    public void setRtgMinPFUnderExcited(Double rtgMinPFUnderExcited) {
        this.rtgMinPFUnderExcited = rtgMinPFUnderExcited;
    }

    public Double getRtgMinV() {
        return rtgMinV;
    }

    public void setRtgMinV(Double rtgMinV) {
        this.rtgMinV = rtgMinV;
    }

    public Integer getRtgNormalCategory() {
        return rtgNormalCategory;
    }

    public void setRtgNormalCategory(Integer rtgNormalCategory) {
        this.rtgNormalCategory = rtgNormalCategory;
    }

    public Double getRtgOverExcitedPF() {
        return rtgOverExcitedPF;
    }

    public void setRtgOverExcitedPF(Double rtgOverExcitedPF) {
        this.rtgOverExcitedPF = rtgOverExcitedPF;
    }

    public Double getRtgOverExcitedW() {
        return rtgOverExcitedW;
    }

    public void setRtgOverExcitedW(Double rtgOverExcitedW) {
        this.rtgOverExcitedW = rtgOverExcitedW;
    }

    public Double getRtgReactiveSusceptance() {
        return rtgReactiveSusceptance;
    }

    public void setRtgReactiveSusceptance(Double rtgReactiveSusceptance) {
        this.rtgReactiveSusceptance = rtgReactiveSusceptance;
    }

    public Double getRtgUnderExcitedPF() {
        return rtgUnderExcitedPF;
    }

    public void setRtgUnderExcitedPF(Double rtgUnderExcitedPF) {
        this.rtgUnderExcitedPF = rtgUnderExcitedPF;
    }

    public Double getRtgUnderExcitedW() {
        return rtgUnderExcitedW;
    }

    public void setRtgUnderExcitedW(Double rtgUnderExcitedW) {
        this.rtgUnderExcitedW = rtgUnderExcitedW;
    }
     
    public Double getRtgVNom() {
        return rtgVNom;
    }
    public void setRtgVNom(Double rtgVNom) {
        this.rtgVNom = rtgVNom;
    } 
     /* Here starts the getters and setters for DER Availability  */


    public Long getAvailabilityDuration() {
        return availabilityDuration;
    }

    public void setAvailabilityDuration(Long availabilityDuration) {
        this.availabilityDuration = availabilityDuration;
    }

    public Long getMaxChargeDuration() {
        return maxChargeDuration;
    }

    public void setMaxChargeDuration(Long maxChargeDuration) {
        this.maxChargeDuration = maxChargeDuration;
    }

    public Double getReserveChargePercent() {
        return reserveChargePercent;
    }

    public void setReserveChargePercent(Double reserveChargePercent) {
        this.reserveChargePercent = reserveChargePercent;
    }

    public Double getReservePercent() {
        return reservePercent;
    }

    public void setReservePercent(Double reservePercent) {
        this.reservePercent = reservePercent;
    }

    public Double getStatVarAvail() {
        return statVarAvail;
    }

    public void setStatVarAvail(Double statVarAvail) {
        this.statVarAvail = statVarAvail;
    }
   
    public Double getStatWAvail() {
        return statWAvail;
    }

    public void setStatWAvail(Double statWAvail) {
        this.statWAvail = statWAvail;
    }
    
    /* Here starts the getters and setters for DER Status  */

    public String getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getGenConnectStatus() {
        return genConnectStatus;
    }

    public void setGenConnectStatus(String genConnectStatus) {
        this.genConnectStatus = genConnectStatus;
    }

    public String getInverterStatus() {
        return inverterStatus;
    }

    public void setInverterStatus(String inverterStatus) {
        this.inverterStatus = inverterStatus;
    }

    public String getLocalControlModeStatus() {
        return localControlModeStatus;
    }

    public void setLocalControlModeStatus(String localControlModeStatus) {
        this.localControlModeStatus = localControlModeStatus;
    }

    public String getManufacturerStatus() {
        return manufacturerStatus;
    }

    public void setManufacturerStatus(String manufacturerStatus) {
        this.manufacturerStatus = manufacturerStatus;
    }

    public String getOperationalModeStatus() {
        return operationalModeStatus;
    }

    public void setOperationalModeStatus(String operationalModeStatus) {
        this.operationalModeStatus = operationalModeStatus;
    }

    public String getReadingTimeStatus() {
        return readingTimeStatus;
    }

    public void setReadingTimeStatus(String readingTimeStatus) {
        this.readingTimeStatus = readingTimeStatus;
    }

    public String getStateOfChargeStatus() {
        return stateOfChargeStatus;
    }

    public void setStateOfChargeStatus(String stateOfChargeStatus) {
        this.stateOfChargeStatus = stateOfChargeStatus;
    }

    public String getStorageModeStatus() {
        return storageModeStatus;
    }

    public void setStorageModeStatus(String storageModeStatus) {
        this.storageModeStatus = storageModeStatus;
    }

    public String getStorConnectStatus() {
        return storConnectStatus;
    }

    public void setStorConnectStatus(String storConnectStatus) {
        this.storConnectStatus = storConnectStatus;
    }
  
}



