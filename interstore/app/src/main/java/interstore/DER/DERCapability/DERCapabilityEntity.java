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
    public record XmlvalueAndMultiplier(Integer multiplier, Integer value) {}
    public record XmlDisplacementAndMultiplier(Integer displacement, Integer multiplier) {}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "modesSupported")
    private String modesSupported;

    @Column(name = "rtgAbnormalCategory")
    private Integer rtgAbnormalCategory;

    // rtgMaxA
    @Column(name = "rtgMaxAMultiplier")
    private Integer rtgMaxAMultiplier;
    @Column(name = "rtgMaxAValue")
    private Integer rtgMaxAValue;

    // rtgMaxAh
    @Column(name = "rtgMaxAhMultiplier")
    private Integer rtgMaxAhMultiplier;
    @Column(name = "rtgMaxAhValue")
    private Integer rtgMaxAhValue;

    // rtgMaxChargeRateVA
    @Column(name = "rtgMaxChargeRateVAMultiplier")
    private Integer rtgMaxChargeRateVAMultiplier;
    @Column(name = "rtgMaxChargeRateVAValue")
    private Integer rtgMaxChargeRateVAValue;

    // rtgMaxChargeRateW
    @Column(name = "rtgMaxChargeRateWMultiplier")
    private Integer rtgMaxChargeRateWMultiplier;
    @Column(name = "rtgMaxChargeRateWValue")
    private Integer rtgMaxChargeRateWValue;

    // rtgMaxDischargeRateW
    @Column(name = "rtgMaxDischargeRateWMultiplier")
    private Integer rtgMaxDischargeRateWMultiplier;
    @Column(name = "rtgMaxDischargeRateWValue")
    private Integer rtgMaxDischargeRateWValue;

    // rtgMaxDischargeRateVA
    @Column(name = "rtgMaxDischargeRateVAMultiplier")
    private Integer rtgMaxDischargeRateVAMultiplier;
    @Column(name = "rtgMaxDischargeRateVAValue")
    private Integer rtgMaxDischargeRateVAValue;

    // rtgMaxV
    @Column(name = "rtgMaxVMultiplier")
    private Integer rtgMaxVMultiplier;
    @Column(name = "rtgMaxVValue")
    private Integer rtgMaxVValue;

    // rtgMaxVA
    @Column(name = "rtgMaxVAMultiplier")
    private Integer rtgMaxVAMultiplier;
    @Column(name = "rtgMaxVAValue")
    private Integer rtgMaxVAValue;

    // rtgMaxVar
    @Column(name = "rtgMaxVarMultiplier")
    private Integer rtgMaxVarMultiplier;
    @Column(name = "rtgMaxVarValue")
    private Integer rtgMaxVarValue;

    // rtgMaxVarNeg
    @Column(name = "rtgMaxVarNegMultiplier")
    private Integer rtgMaxVarNegMultiplier;
    @Column(name = "rtgMaxVarNegValue")
    private Integer rtgMaxVarNegValue;

    // rtgMaxW
    @Column(name = "rtgMaxWMultiplier")
    private Integer rtgMaxWMultiplier;
    @Column(name = "rtgMaxWValue")
    private Integer rtgMaxWValue;

    // rtgMaxWh
    @Column(name = "rtgMaxWhMultiplier")
    private Integer rtgMaxWhMultiplier;
    @Column(name = "rtgMaxWhValue")
    private Integer rtgMaxWhValue;

    // rtgMinPFOverExcited (displacement + multiplier)
    @Column(name = "rtgMinPFOverExcitedDisplacement")
    private Integer rtgMinPFOverExcitedDisplacement;
    @Column(name = "rtgMinPFOverExcitedMultiplier")
    private Integer rtgMinPFOverExcitedMultiplier;

    // rtgMinPFUnderExcited (displacement + multiplier)
    @Column(name = "rtgMinPFUnderExcitedDisplacement")
    private Integer rtgMinPFUnderExcitedDisplacement;
    @Column(name = "rtgMinPFUnderExcitedMultiplier")
    private Integer rtgMinPFUnderExcitedMultiplier;

    // rtgMinV
    @Column(name = "rtgMinVMultiplier")
    private Integer rtgMinVMultiplier;
    @Column(name = "rtgMinVValue")
    private Integer rtgMinVValue;

    @Column(name = "rtgNormalCategory")
    private Integer rtgNormalCategory;

    // rtgOverExcitedPF (displacement + multiplier)
    @Column(name = "rtgOverExcitedPFDisplacement")
    private Integer rtgOverExcitedPFDisplacement;
    @Column(name = "rtgOverExcitedPFMultiplier")
    private Integer rtgOverExcitedPFMultiplier;

    // rtgOverExcitedW
    @Column(name = "rtgOverExcitedWMultiplier")
    private Integer rtgOverExcitedWMultiplier;
    @Column(name = "rtgOverExcitedWValue")
    private Integer rtgOverExcitedWValue;

    // rtgReactiveSusceptance
    @Column(name = "rtgReactiveSusceptanceMultiplier")
    private Integer rtgReactiveSusceptanceMultiplier;
    @Column(name = "rtgReactiveSusceptanceValue")
    private Integer rtgReactiveSusceptanceValue;

    // rtgUnderExcitedPF (displacement + multiplier)
    @Column(name = "rtgUnderExcitedPFDisplacement")
    private Integer rtgUnderExcitedPFDisplacement;
    @Column(name = "rtgUnderExcitedPFMultiplier")
    private Integer rtgUnderExcitedPFMultiplier;

    // rtgUnderExcitedW
    @Column(name = "rtgUnderExcitedWMultiplier")
    private Integer rtgUnderExcitedWMultiplier;
    @Column(name = "rtgUnderExcitedWValue")
    private Integer rtgUnderExcitedWValue;

    // rtgVNom
    @Column(name = "rtgVNomMultiplier")
    private Integer rtgVNomMultiplier;
    @Column(name = "rtgVNomValue")
    private Integer rtgVNomValue;

    @Column(name = "type")
    private Integer derType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_entity")
    private DerEntity derEntity;

    @Column(name = "derCapabilityLink")
    private String derCapabilityLink;

    // ============ SETTERS FOR VALUE + MULTIPLIER FIELDS ============

    public void setRtgMaxA(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxAMultiplier = xml.multiplier();
            this.rtgMaxAValue = xml.value();
        } else {
            this.rtgMaxAMultiplier = null;
            this.rtgMaxAValue = null;
        }
    }

    public void setRtgMaxAh(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxAhMultiplier = xml.multiplier();
            this.rtgMaxAhValue = xml.value();
        } else {
            this.rtgMaxAhMultiplier = null;
            this.rtgMaxAhValue = null;
        }
    }

    public void setRtgMaxChargeRateVA(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxChargeRateVAMultiplier = xml.multiplier();
            this.rtgMaxChargeRateVAValue = xml.value();
        } else {
            this.rtgMaxChargeRateVAMultiplier = null;
            this.rtgMaxChargeRateVAValue = null;
        }
    }

    public void setRtgMaxChargeRateW(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxChargeRateWMultiplier = xml.multiplier();
            this.rtgMaxChargeRateWValue = xml.value();
        } else {
            this.rtgMaxChargeRateWMultiplier = null;
            this.rtgMaxChargeRateWValue = null;
        }
    }

    public void setRtgMaxDischargeRateW(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxDischargeRateWMultiplier = xml.multiplier();
            this.rtgMaxDischargeRateWValue = xml.value();
        } else {
            this.rtgMaxDischargeRateWMultiplier = null;
            this.rtgMaxDischargeRateWValue = null;
        }
    }

    public void setRtgMaxDischargeRateVA(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxDischargeRateVAMultiplier = xml.multiplier();
            this.rtgMaxDischargeRateVAValue = xml.value();
        } else {
            this.rtgMaxDischargeRateVAMultiplier = null;
            this.rtgMaxDischargeRateVAValue = null;
        }
    }

    public void setRtgMaxV(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxVMultiplier = xml.multiplier();
            this.rtgMaxVValue = xml.value();
        } else {
            this.rtgMaxVMultiplier = null;
            this.rtgMaxVValue = null;
        }
    }

    public void setRtgMaxVA(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxVAMultiplier = xml.multiplier();
            this.rtgMaxVAValue = xml.value();
        } else {
            this.rtgMaxVAMultiplier = null;
            this.rtgMaxVAValue = null;
        }
    }

    public void setRtgMaxVar(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxVarMultiplier = xml.multiplier();
            this.rtgMaxVarValue = xml.value();
        } else {
            this.rtgMaxVarMultiplier = null;
            this.rtgMaxVarValue = null;
        }
    }

    public void setRtgMaxVarNeg(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxVarNegMultiplier = xml.multiplier();
            this.rtgMaxVarNegValue = xml.value();
        } else {
            this.rtgMaxVarNegMultiplier = null;
            this.rtgMaxVarNegValue = null;
        }
    }

    public void setRtgMaxW(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxWMultiplier = xml.multiplier();
            this.rtgMaxWValue = xml.value();
        } else {
            this.rtgMaxWMultiplier = null;
            this.rtgMaxWValue = null;
        }
    }

    public void setRtgMaxWh(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMaxWhMultiplier = xml.multiplier();
            this.rtgMaxWhValue = xml.value();
        } else {
            this.rtgMaxWhMultiplier = null;
            this.rtgMaxWhValue = null;
        }
    }

    public void setRtgMinV(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgMinVMultiplier = xml.multiplier();
            this.rtgMinVValue = xml.value();
        } else {
            this.rtgMinVMultiplier = null;
            this.rtgMinVValue = null;
        }
    }

    public void setRtgOverExcitedW(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgOverExcitedWMultiplier = xml.multiplier();
            this.rtgOverExcitedWValue = xml.value();
        } else {
            this.rtgOverExcitedWMultiplier = null;
            this.rtgOverExcitedWValue = null;
        }
    }

    public void setRtgReactiveSusceptance(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgReactiveSusceptanceMultiplier = xml.multiplier();
            this.rtgReactiveSusceptanceValue = xml.value();
        } else {
            this.rtgReactiveSusceptanceMultiplier = null;
            this.rtgReactiveSusceptanceValue = null;
        }
    }

    public void setRtgUnderExcitedW(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgUnderExcitedWMultiplier = xml.multiplier();
            this.rtgUnderExcitedWValue = xml.value();
        } else {
            this.rtgUnderExcitedWMultiplier = null;
            this.rtgUnderExcitedWValue = null;
        }
    }

    public void setRtgVNom(XmlvalueAndMultiplier xml) {
        if (xml != null) {
            this.rtgVNomMultiplier = xml.multiplier();
            this.rtgVNomValue = xml.value();
        } else {
            this.rtgVNomMultiplier = null;
            this.rtgVNomValue = null;
        }
    }

    // ============ SETTERS FOR DISPLACEMENT + MULTIPLIER FIELDS ============

    public void setRtgMinPFOverExcited(XmlDisplacementAndMultiplier xml) {
        if (xml != null) {
            this.rtgMinPFOverExcitedDisplacement = xml.displacement();
            this.rtgMinPFOverExcitedMultiplier = xml.multiplier();
        } else {
            this.rtgMinPFOverExcitedDisplacement = null;
            this.rtgMinPFOverExcitedMultiplier = null;
        }
    }

    public void setRtgMinPFUnderExcited(XmlDisplacementAndMultiplier xml) {
        if (xml != null) {
            this.rtgMinPFUnderExcitedDisplacement = xml.displacement();
            this.rtgMinPFUnderExcitedMultiplier = xml.multiplier();
        } else {
            this.rtgMinPFUnderExcitedDisplacement = null;
            this.rtgMinPFUnderExcitedMultiplier = null;
        }
    }

    public void setRtgOverExcitedPF(XmlDisplacementAndMultiplier xml) {
        if (xml != null) {
            this.rtgOverExcitedPFDisplacement = xml.displacement();
            this.rtgOverExcitedPFMultiplier = xml.multiplier();
        } else {
            this.rtgOverExcitedPFDisplacement = null;
            this.rtgOverExcitedPFMultiplier = null;
        }
    }

    public void setRtgUnderExcitedPF(XmlDisplacementAndMultiplier xml) {
        if (xml != null) {
            this.rtgUnderExcitedPFDisplacement = xml.displacement();
            this.rtgUnderExcitedPFMultiplier = xml.multiplier();
        } else {
            this.rtgUnderExcitedPFDisplacement = null;
            this.rtgUnderExcitedPFMultiplier = null;
        }
    }

    // ============ SIMPLE SETTERS ============

    public void setModesSupported(String modesSupported) {
        this.modesSupported = modesSupported;
    }

    public void setRtgAbnormalCategory(Integer rtgAbnormalCategory) {
        this.rtgAbnormalCategory = rtgAbnormalCategory;
    }

    public void setRtgNormalCategory(Integer rtgNormalCategory) {
        this.rtgNormalCategory = rtgNormalCategory;
    }

    public void setDerType(Integer derType) {
        this.derType = derType;
    }

    public void setDerCapabilityLink(String derCapabilityLink) {
        this.derCapabilityLink = derCapabilityLink;
    }

    public void setDerEntity(DerEntity derEntity) {
        this.derEntity = derEntity;
    }

    // ============ GETTERS ============
    
    public Long getId() { return id; }
    public String getModesSupported() { return modesSupported; }
    public Integer getRtgAbnormalCategory() { return rtgAbnormalCategory; }
    public Integer getRtgMaxAMultiplier() { return rtgMaxAMultiplier; }
    public Integer getRtgMaxAValue() { return rtgMaxAValue; }
    public Integer getRtgMaxAhMultiplier() { return rtgMaxAhMultiplier; }
    public Integer getRtgMaxAhValue() { return rtgMaxAhValue; }
    public Integer getRtgMaxChargeRateVAMultiplier() { return rtgMaxChargeRateVAMultiplier; }
    public Integer getRtgMaxChargeRateVAValue() { return rtgMaxChargeRateVAValue; }
    public Integer getRtgMaxChargeRateWMultiplier() { return rtgMaxChargeRateWMultiplier; }
    public Integer getRtgMaxChargeRateWValue() { return rtgMaxChargeRateWValue; }
    public Integer getRtgMaxDischargeRateWMultiplier() { return rtgMaxDischargeRateWMultiplier; }
    public Integer getRtgMaxDischargeRateWValue() { return rtgMaxDischargeRateWValue; }
    public Integer getRtgMaxDischargeRateVAMultiplier() { return rtgMaxDischargeRateVAMultiplier; }
    public Integer getRtgMaxDischargeRateVAValue() { return rtgMaxDischargeRateVAValue; }
    public Integer getRtgMaxVMultiplier() { return rtgMaxVMultiplier; }
    public Integer getRtgMaxVValue() { return rtgMaxVValue; }
    public Integer getRtgMaxVAMultiplier() { return rtgMaxVAMultiplier; }
    public Integer getRtgMaxVAValue() { return rtgMaxVAValue; }
    public Integer getRtgMaxVarMultiplier() { return rtgMaxVarMultiplier; }
    public Integer getRtgMaxVarValue() { return rtgMaxVarValue; }
    public Integer getRtgMaxVarNegMultiplier() { return rtgMaxVarNegMultiplier; }
    public Integer getRtgMaxVarNegValue() { return rtgMaxVarNegValue; }
    public Integer getRtgMaxWMultiplier() { return rtgMaxWMultiplier; }
    public Integer getRtgMaxWValue() { return rtgMaxWValue; }
    public Integer getRtgMaxWhMultiplier() { return rtgMaxWhMultiplier; }
    public Integer getRtgMaxWhValue() { return rtgMaxWhValue; }
    public Integer getRtgMinPFOverExcitedDisplacement() { return rtgMinPFOverExcitedDisplacement; }
    public Integer getRtgMinPFOverExcitedMultiplier() { return rtgMinPFOverExcitedMultiplier; }
    public Integer getRtgMinPFUnderExcitedDisplacement() { return rtgMinPFUnderExcitedDisplacement; }
    public Integer getRtgMinPFUnderExcitedMultiplier() { return rtgMinPFUnderExcitedMultiplier; }
    public Integer getRtgMinVMultiplier() { return rtgMinVMultiplier; }
    public Integer getRtgMinVValue() { return rtgMinVValue; }
    public Integer getRtgNormalCategory() { return rtgNormalCategory; }
    public Integer getRtgOverExcitedPFDisplacement() { return rtgOverExcitedPFDisplacement; }
    public Integer getRtgOverExcitedPFMultiplier() { return rtgOverExcitedPFMultiplier; }
    public Integer getRtgOverExcitedWMultiplier() { return rtgOverExcitedWMultiplier; }
    public Integer getRtgOverExcitedWValue() { return rtgOverExcitedWValue; }
    public Integer getRtgReactiveSusceptanceMultiplier() { return rtgReactiveSusceptanceMultiplier; }
    public Integer getRtgReactiveSusceptanceValue() { return rtgReactiveSusceptanceValue; }
    public Integer getRtgUnderExcitedPFDisplacement() { return rtgUnderExcitedPFDisplacement; }
    public Integer getRtgUnderExcitedPFMultiplier() { return rtgUnderExcitedPFMultiplier; }
    public Integer getRtgUnderExcitedWMultiplier() { return rtgUnderExcitedWMultiplier; }
    public Integer getRtgUnderExcitedWValue() { return rtgUnderExcitedWValue; }
    public Integer getRtgVNomMultiplier() { return rtgVNomMultiplier; }
    public Integer getRtgVNomValue() { return rtgVNomValue; }
    public Integer getDerType() { return derType; }
    public DerEntity getDerEntity() { return derEntity; }
    public String getDerCapabilityLink() { return derCapabilityLink; }
}

