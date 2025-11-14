package interstore.EndDevice.DeviceInformation;

import interstore.EndDevice.EndDeviceEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class DeviceInformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "lFDI")
    private String lFDI;

    @Column(name = "mf_date")
    private Long mfDate;

    @Column(name = "mf_hw_ver")
    private String mfHwVer;

    @Column(name = "mf_id")
    private String mfID;

    @Column(name = "mf_info")
    private String mfInfo;

    @Column(name = "mf_model")
    private String mfModel;

    @Column(name = "mf_ser_num")
    private String mfSerNum;

    @Column(name = "primary_power")
    private String primaryPower;

    @Column(name = "secondary_power")
    private String secondaryPower;

    @Column(name = "sw_act_time")
    private Long swActTime;

    @Column(name = "sw_ver")
    private String swVer;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "end_device_id")
    private EndDeviceEntity endDeviceEntity;

    public Long getId() {
        return id;
    }

    public Long getMfDate() {
        return mfDate;
    }

    public void setMfDate(Long mfDate) {
        this.mfDate = mfDate;
    }

    public String getMfHwVer() {
        return mfHwVer;
    }

    public void setMfHwVer(String mfHwVer) {
        this.mfHwVer = mfHwVer;
    }

    public String getMfID() {
        return mfID;
    }

    public void setMfID(String mfID) {
        this.mfID = mfID;
    }

    public String getMfInfo() {
        return mfInfo;
    }

    public void setMfInfo(String mfInfo) {
        this.mfInfo = mfInfo;
    }

    public String getMfModel() {
        return mfModel;
    }

    public void setMfModel(String mfModel) {
        this.mfModel = mfModel;
    }

    public String getMfSerNum() {
        return mfSerNum;
    }

    public void setMfSerNum(String mfSerNum) {
        this.mfSerNum = mfSerNum;
    }

    public String getPrimaryPower() {
        return primaryPower;
    }

    public void setPrimaryPower(String primaryPower) {
        this.primaryPower = primaryPower;
    }

    public String getSecondaryPower() {
        return secondaryPower;
    }

    public void setSecondaryPower(String secondaryPower) {
        this.secondaryPower = secondaryPower;
    }

    public Long getSwActTime() {
        return swActTime;
    }

    public void setSwActTime(Long swActTime) {
        this.swActTime = swActTime;
    }

    public String getSwVer() {
        return swVer;
    }

    public void setSwVer(String swVer) {
        this.swVer = swVer;
    }

    public EndDeviceEntity getEndDeviceEntity() {
        return endDeviceEntity;
    }

    public void setEndDeviceEntity(EndDeviceEntity endDeviceEntity) {
        this.endDeviceEntity = endDeviceEntity;
    }
    
    public String getlFDI() {
        return lFDI;
    }

    public void setlFDI(String lFDI) {
        this.lFDI = lFDI;
    }
}
