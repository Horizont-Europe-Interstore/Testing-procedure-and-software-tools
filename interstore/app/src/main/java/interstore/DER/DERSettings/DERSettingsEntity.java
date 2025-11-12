package interstore.DER.DERSettings;

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
public class DERSettingsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_entity") 
    private DerEntity derEntity;

    @Column(name = "derSettingsLink")
    private String derSettingsLink;

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

    public Double getSetVRefOfs() {
        return setVRefOfs;
    }

    public void setSetVRefOfs(Double setVRefOfs) {
        this.setVRefOfs = setVRefOfs;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
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

    public String getDerSettingsLink() {
        return derSettingsLink;
    }

    public void setDerSettingsLink(String derSettingsLink) {
        this.derSettingsLink = derSettingsLink;
    }
}
