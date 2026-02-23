package interstore.DER.DERStatus;

import interstore.DER.DerEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class DERStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "alarmStatus")
    private String alarmStatus;  
    
    @Column(name = "genConnectStatusDateTime")
    private Long genConnectStatusDateTime; 
    @Column(name = "genConnectStatusValue")
    private Integer genConnectStatusValue;

    @Column(name = "inverterStatusDateTime")
    private Long inverterStatusDateTime; 
    @Column(name = "inverterStatusValue")
    private Integer inverterStatusValue;

    @Column(name = "localControlModeStatusDateTime")
    private Long localControlModeStatusDateTime;
    @Column(name = "localControlModeStatusValue")
    private Integer localControlModeStatusValue;

    @Column(name = "manufacturerStatus")
    private String manufacturerStatus;  

    @Column(name = "operationalModeStatusDateTime")
    private Long operationalModeStatusDateTime; 
    @Column(name = "operationalModeStatusValue")
    private Integer operationalModeStatusValue;

    @Column(name = "readingTime")
    private Long readingTime;  

    @Column(name = "stateOfChargeStatusDateTime")
    private Long stateOfChargeStatusDateTime;  
    @Column(name = "stateOfChargeStatusValue")
    private Integer stateOfChargeStatusValue;

    @Column(name = "storageModeStatusDateTime")
    private Long storageModeStatusDateTime;
    @Column(name = "storageModeStatusValue")
    private Integer storageModeStatusValue;

    @Column(name = "storConnectStatusDateTime")
    private Long storConnectStatusDateTime;
    @Column(name = "storConnectStatusValue")
    private Integer storConnectStatusValue;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_entity") 
    private DerEntity derEntity;

    @Column(name = "derStatusLink")
    private String derStatusLink;

    public String getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public Long getGenConnectStatusDateTime() {
        return genConnectStatusDateTime;
    }

    public void setGenConnectStatusDateTime(Long genConnectStatusDateTime) {
        this.genConnectStatusDateTime = genConnectStatusDateTime;
    }

    public Integer getGenConnectStatusValue() {
        return genConnectStatusValue;
    }

    public void setGenConnectStatusValue(Integer genConnectStatusValue) {
        this.genConnectStatusValue = genConnectStatusValue;
    }

    public Long getInverterStatusDateTime() {
        return inverterStatusDateTime;
    }

    public void setInverterStatusDateTime(Long inverterStatusDateTime) {
        this.inverterStatusDateTime = inverterStatusDateTime;
    }

    public Integer getInverterStatusValue() {
        return inverterStatusValue;
    }

    public void setInverterStatusValue(Integer inverterStatusValue) {
        this.inverterStatusValue = inverterStatusValue;
    }

    public Long getLocalControlModeStatusDateTime() {
        return localControlModeStatusDateTime;
    }

    public void setLocalControlModeStatusDateTime(Long localControlModeStatusDateTime) {
        this.localControlModeStatusDateTime = localControlModeStatusDateTime;
    }

    public Integer getLocalControlModeStatusValue() {
        return localControlModeStatusValue;
    }

    public void setLocalControlModeStatusValue(Integer localControlModeStatusValue) {
        this.localControlModeStatusValue = localControlModeStatusValue;
    }

    public String getManufacturerStatus() {
        return manufacturerStatus;
    }

    public void setManufacturerStatus(String manufacturerStatus) {
        this.manufacturerStatus = manufacturerStatus;
    }

    public Long getOperationalModeStatusDateTime() {
        return operationalModeStatusDateTime;
    }

    public void setOperationalModeStatusDateTime(Long operationalModeStatusDateTime) {
        this.operationalModeStatusDateTime = operationalModeStatusDateTime;
    }

    public Integer getOperationalModeStatusValue() {
        return operationalModeStatusValue;
    }

    public void setOperationalModeStatusValue(Integer operationalModeStatusValue) {
        this.operationalModeStatusValue = operationalModeStatusValue;
    }

    public Long getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(Long readingTime) {
        this.readingTime = readingTime;
    }

    public Long getStateOfChargeStatusDateTime() {
        return stateOfChargeStatusDateTime;
    }

    public void setStateOfChargeStatusDateTime(Long stateOfChargeStatusDateTime) {
        this.stateOfChargeStatusDateTime = stateOfChargeStatusDateTime;
    }

    public Integer getStateOfChargeStatusValue() {
        return stateOfChargeStatusValue;
    }

    public void setStateOfChargeStatusValue(Integer stateOfChargeStatusValue) {
        this.stateOfChargeStatusValue = stateOfChargeStatusValue;
    }

    public Long getStorageModeStatusDateTime() {
        return storageModeStatusDateTime;
    }

    public void setStorageModeStatusDateTime(Long storageModeStatusDateTime) {
        this.storageModeStatusDateTime = storageModeStatusDateTime;
    }

    public Integer getStorageModeStatusValue() {
        return storageModeStatusValue;
    }

    public void setStorageModeStatusValue(Integer storageModeStatusValue) {
        this.storageModeStatusValue = storageModeStatusValue;
    }

    public Long getStorConnectStatusDateTime() {
        return storConnectStatusDateTime;
    }

    public void setStorConnectStatusDateTime(Long storConnectStatusDateTime) {
        this.storConnectStatusDateTime = storConnectStatusDateTime;
    }

    public Integer getStorConnectStatusValue() {
        return storConnectStatusValue;
    }

    public void setStorConnectStatusValue(Integer storConnectStatusValue) {
        this.storConnectStatusValue = storConnectStatusValue;
    }

    public Long getId() {
        return id;
    }

    public DerEntity getDerEntity() {
        return derEntity;
    }

    public void setDerEntity(DerEntity derEntity) {
        this.derEntity = derEntity;
    }

    public String getDerStatusLink() {
        return derStatusLink;
    }

    public void setDerStatusLink(String derStatusLink) {
        this.derStatusLink = derStatusLink;
    }
}
