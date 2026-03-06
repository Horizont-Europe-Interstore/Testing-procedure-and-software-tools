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
    public record XmlvalueAndMultiplierSettings(Integer multiplier, Integer value) {}
    public record XmlDisplacementAndMultiplierSettings(Integer displacement, Integer multiplier) {}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Simple fields
    @Column(name = "modesEnabled")
    private String modesEnabled;

    @Column(name = "setESDelay")
    private Long setESDelay;

    @Column(name = "setESHighFreq")
    private Long setESHighFreq;

    @Column(name = "setESHighVolt")
    private Long setESHighVolt;

    @Column(name = "setESLowFreq")
    private Long setESLowFreq;

    @Column(name = "setESLowVolt")
    private Long setESLowVolt;

    @Column(name = "setESRampTms")
    private Long setESRampTms;

    @Column(name = "setESRandomDelay")
    private Long setESRandomDelay;

    @Column(name = "setGradW")
    private Long setGradW;

    @Column(name = "setSoftGradW")
    private Long setSoftGradW;

    @Column(name = "updatedTime")
    private Long updatedTime;

    // setMaxDischargeRateW
    @Column(name = "setMaxDischargeRateWValue")
    private Integer setMaxDischargeRateWValue;
    @Column(name = "setMaxDischargeRateWMultiplier")
    private Integer setMaxDischargeRateWMultiplier;

    // setMaxV
    @Column(name = "setMaxVValue")
    private Integer setMaxVValue;
    @Column(name = "setMaxVMultiplier")
    private Integer setMaxVMultiplier;

    // setMaxVA
    @Column(name = "setMaxVAValue")
    private Integer setMaxVAValue;
    @Column(name = "setMaxVAMultiplier")
    private Integer setMaxVAMultiplier;

    // setMaxVar
    @Column(name = "setMaxVarValue")
    private Integer setMaxVarValue;
    @Column(name = "setMaxVarMultiplier")
    private Integer setMaxVarMultiplier;

    // setMaxVarNeg
    @Column(name = "setMaxVarNegValue")
    private Integer setMaxVarNegValue;
    @Column(name = "setMaxVarNegMultiplier")
    private Integer setMaxVarNegMultiplier;

    // setMaxW
    @Column(name = "setMaxWValue")
    private Integer setMaxWValue;
    @Column(name = "setMaxWMultiplier")
    private Integer setMaxWMultiplier;

    // setMinV
    @Column(name = "setMinVValue")
    private Integer setMinVValue;
    @Column(name = "setMinVMultiplier")
    private Integer setMinVMultiplier;

    // setVNom
    @Column(name = "setVNomValue")
    private Integer setVNomValue;
    @Column(name = "setVNomMultiplier")
    private Integer setVNomMultiplier;

    // setVRef
    @Column(name = "setVRefValue")
    private Integer setVRefValue;
    @Column(name = "setVRefMultiplier")
    private Integer setVRefMultiplier;

    // setMinPFOverExcited (displacement + multiplier)
    @Column(name = "setMinPFOverExcitedDisplacement")
    private Integer setMinPFOverExcitedDisplacement;
    @Column(name = "setMinPFOverExcitedMultiplier")
    private Integer setMinPFOverExcitedMultiplier;

    // setMinPFUnderExcited (displacement + multiplier)
    @Column(name = "setMinPFUnderExcitedDisplacement")
    private Integer setMinPFUnderExcitedDisplacement;
    @Column(name = "setMinPFUnderExcitedMultiplier")
    private Integer setMinPFUnderExcitedMultiplier;

    // ============ SIMPLE SETTERS ============

    public void setModesEnabled(String modesEnabled) {
        this.modesEnabled = modesEnabled;
    }

    public void setSetESDelay(Long setESDelay) {
        this.setESDelay = setESDelay;
    }

    public void setSetESHighFreq(Long setESHighFreq) {
        this.setESHighFreq = setESHighFreq;
    }

    public void setSetESHighVolt(Long setESHighVolt) {
        this.setESHighVolt = setESHighVolt;
    }

    public void setSetESLowFreq(Long setESLowFreq) {
        this.setESLowFreq = setESLowFreq;
    }

    public void setSetESLowVolt(Long setESLowVolt) {
        this.setESLowVolt = setESLowVolt;
    }

    public void setSetESRampTms(Long setESRampTms) {
        this.setESRampTms = setESRampTms;
    }

    public void setSetESRandomDelay(Long setESRandomDelay) {
        this.setESRandomDelay = setESRandomDelay;
    }

    public void setSetGradW(Long setGradW) {
        this.setGradW = setGradW;
    }

    public void setSetSoftGradW(Long setSoftGradW) {
        this.setSoftGradW = setSoftGradW;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    // ============ VALUE + MULTIPLIER SETTERS ============

    public void setSetMaxDischargeRateW(XmlvalueAndMultiplierSettings xml) {
        if (xml != null) {
            this.setMaxDischargeRateWValue = xml.value();
            this.setMaxDischargeRateWMultiplier = xml.multiplier();
        } else {
            this.setMaxDischargeRateWValue = null;
            this.setMaxDischargeRateWMultiplier = null;
        }
    }

    public void setSetMaxV(XmlvalueAndMultiplierSettings xml) {
        if (xml != null) {
            this.setMaxVValue = xml.value();
            this.setMaxVMultiplier = xml.multiplier();
        } else {
            this.setMaxVValue = null;
            this.setMaxVMultiplier = null;
        }
    }

    public void setSetMaxVA(XmlvalueAndMultiplierSettings xml) {
        if (xml != null) {
            this.setMaxVAValue = xml.value();
            this.setMaxVAMultiplier = xml.multiplier();
        } else {
            this.setMaxVAValue = null;
            this.setMaxVAMultiplier = null;
        }
    }

    public void setSetMaxVar(XmlvalueAndMultiplierSettings xml) {
        if (xml != null) {
            this.setMaxVarValue = xml.value();
            this.setMaxVarMultiplier = xml.multiplier();
        } else {
            this.setMaxVarValue = null;
            this.setMaxVarMultiplier = null;
        }
    }

    public void setSetMaxVarNeg(XmlvalueAndMultiplierSettings xml) {
        if (xml != null) {
            this.setMaxVarNegValue = xml.value();
            this.setMaxVarNegMultiplier = xml.multiplier();
        } else {
            this.setMaxVarNegValue = null;
            this.setMaxVarNegMultiplier = null;
        }
    }

    public void setSetMaxW(XmlvalueAndMultiplierSettings xml) {
        if (xml != null) {
            this.setMaxWValue = xml.value();
            this.setMaxWMultiplier = xml.multiplier();
        } else {
            this.setMaxWValue = null;
            this.setMaxWMultiplier = null;
        }
    }

    public void setSetMinV(XmlvalueAndMultiplierSettings xml) {
        if (xml != null) {
            this.setMinVValue = xml.value();
            this.setMinVMultiplier = xml.multiplier();
        } else {
            this.setMinVValue = null;
            this.setMinVMultiplier = null;
        }
    }

    public void setSetVNom(XmlvalueAndMultiplierSettings xml) {
        if (xml != null) {
            this.setVNomValue = xml.value();
            this.setVNomMultiplier = xml.multiplier();
        } else {
            this.setVNomValue = null;
            this.setVNomMultiplier = null;
        }
    }

    public void setSetVRef(XmlvalueAndMultiplierSettings xml) {
        if (xml != null) {
            this.setVRefValue = xml.value();
            this.setVRefMultiplier = xml.multiplier();
        } else {
            this.setVRefValue = null;
            this.setVRefMultiplier = null;
        }
    }

    // ============ DISPLACEMENT + MULTIPLIER SETTERS ============

    public void setSetMinPFOverExcited(XmlDisplacementAndMultiplierSettings xml) {
        if (xml != null) {
            this.setMinPFOverExcitedDisplacement = xml.displacement();
            this.setMinPFOverExcitedMultiplier = xml.multiplier();
        } else {
            this.setMinPFOverExcitedDisplacement = null;
            this.setMinPFOverExcitedMultiplier = null;
        }
    }

    public void setSetMinPFUnderExcited(XmlDisplacementAndMultiplierSettings xml) {
        if (xml != null) {
            this.setMinPFUnderExcitedDisplacement = xml.displacement();
            this.setMinPFUnderExcitedMultiplier = xml.multiplier();
        } else {
            this.setMinPFUnderExcitedDisplacement = null;
            this.setMinPFUnderExcitedMultiplier = null;
        }
    }

    // ============ GETTERS ============

    public Long getId() { return id; }
    public String getModesEnabled() { return modesEnabled; }
    public Long getSetESDelay() { return setESDelay; }
    public Long getSetESHighFreq() { return setESHighFreq; }
    public Long getSetESHighVolt() { return setESHighVolt; }
    public Long getSetESLowFreq() { return setESLowFreq; }
    public Long getSetESLowVolt() { return setESLowVolt; }
    public Long getSetESRampTms() { return setESRampTms; }
    public Long getSetESRandomDelay() { return setESRandomDelay; }
    public Long getSetGradW() { return setGradW; }
    public Long getSetSoftGradW() { return setSoftGradW; }
    public Long getUpdatedTime() { return updatedTime; }
    public Integer getSetMaxDischargeRateWValue() { return setMaxDischargeRateWValue; }
    public Integer getSetMaxDischargeRateWMultiplier() { return setMaxDischargeRateWMultiplier; }
    public Integer getSetMaxVValue() { return setMaxVValue; }
    public Integer getSetMaxVMultiplier() { return setMaxVMultiplier; }
    public Integer getSetMaxVAValue() { return setMaxVAValue; }
    public Integer getSetMaxVAMultiplier() { return setMaxVAMultiplier; }
    public Integer getSetMaxVarValue() { return setMaxVarValue; }
    public Integer getSetMaxVarMultiplier() { return setMaxVarMultiplier; }
    public Integer getSetMaxVarNegValue() { return setMaxVarNegValue; }
    public Integer getSetMaxVarNegMultiplier() { return setMaxVarNegMultiplier; }
    public Integer getSetMaxWValue() { return setMaxWValue; }
    public Integer getSetMaxWMultiplier() { return setMaxWMultiplier; }
    public Integer getSetMinVValue() { return setMinVValue; }
    public Integer getSetMinVMultiplier() { return setMinVMultiplier; }
    public Integer getSetVNomValue() { return setVNomValue; }
    public Integer getSetVNomMultiplier() { return setVNomMultiplier; }
    public Integer getSetVRefValue() { return setVRefValue; }
    public Integer getSetVRefMultiplier() { return setVRefMultiplier; }
    public Integer getSetMinPFOverExcitedDisplacement() { return setMinPFOverExcitedDisplacement; }
    public Integer getSetMinPFOverExcitedMultiplier() { return setMinPFOverExcitedMultiplier; }
    public Integer getSetMinPFUnderExcitedDisplacement() { return setMinPFUnderExcitedDisplacement; }
    public Integer getSetMinPFUnderExcitedMultiplier() { return setMinPFUnderExcitedMultiplier; }
}