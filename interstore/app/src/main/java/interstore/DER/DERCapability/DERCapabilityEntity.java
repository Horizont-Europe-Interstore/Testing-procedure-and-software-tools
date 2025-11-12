package interstore.DER.DERCapability;

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
public class DERCapabilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_entity") 
    private DerEntity derEntity;

    @Column(name = "derCapabilityLink")
    private String derCapabilityLink;


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

    public Long getId() {
        return id;
    }

    public DerEntity getDerEntity() {
        return derEntity;
    }

    public void setDerEntity(DerEntity derEntity) {
        this.derEntity = derEntity;
    }

    public String getDerCapabilityLink() {
        return derCapabilityLink;
    }

    public void setDerCapabilityLink(String derCapabilityLink) {
        this.derCapabilityLink = derCapabilityLink;
    }

}
