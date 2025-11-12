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
