package interstore.DERControl;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "DER_Control_Base")
public class DERControlBase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "opModConnect")
    private Boolean opModConnect;

    @Column(name = "opModEnergize")
    private Boolean opModEnergize;

    @Column(name = "opModFixedPFAbsorbW")
    private Integer opModFixedPFAbsorbW;

    @Column(name = "opModFixedPFInjectW")
    private Integer opModFixedPFInjectW;

    @Column(name = "opModFixedVar")
    private Integer opModFixedVar;

    @Column(name = "opModFixedW")
    private Integer opModFixedW;

    @Column(name = "opModFreqDroop")
    private Integer opModFreqDroop;

    @Column(name = "opModFreqWatt")
    private String opModFreqWatt;

    @Column(name = "opModHFRTMayTrip")
    private String opModHFRTMayTrip;

    @Column(name = "opModHFRTMustTrip")
    private String opModHFRTMustTrip;

    @Column(name = "opModHVRTMayTrip")
    private String opModHVRTMayTrip;

    @Column(name = "opModHVRTMomentaryCessation")
    private String opModHVRTMomentaryCessation;

    @Column(name = "opModHVRTMustTrip")
    private String opModHVRTMustTrip;

    @Column(name = "opModLFRTMayTrip")
    private String opModLFRTMayTrip;

    @Column(name = "opModLFRTMustTrip")
    private String opModLFRTMustTrip;

    @Column(name = "opModLVRTMayTrip")
    private String opModLVRTMayTrip;

    @Column(name = "opModLVRTMomentaryCessation")
    private String opModLVRTMomentaryCessation;

    @Column(name = "opModLVRTMustTrip")
    private String opModLVRTMustTrip;

    @Column(name = "opModMaxLimW")
    private Integer IntegeropModMaxLimW;

    @Column(name = "opModTargetVar")
    private Integer opModTargetVar;

    @Column(name = "opModTargetW")
    private Integer opModTargetW;

    @Column(name = "opModVoltVar")
    private String opModVoltVar;

    @Column(name = "opModVoltWatt")
    private String opModVoltWatt;

    @Column(name = "opModWattPF")
    private String opModWattPF;

    @Column(name = "opModWattVar")
    private String opModWattVar;

    @Column(name = "rampTms")
    private Integer rampTms;

    public DERControlBase() {
    }

    public Long getId() {
        return id;
    }


    public Boolean getOpModConnect() {
        return opModConnect;
    }

    public void setOpModConnect(Boolean opModConnect) {
        this.opModConnect = opModConnect;
    }

    public Boolean getOpModEnergize() {
        return opModEnergize;
    }

    public void setOpModEnergize(Boolean opModEnergize) {
        this.opModEnergize = opModEnergize;
    }

    public Integer getOpModFixedPFAbsorbW() {
        return opModFixedPFAbsorbW;
    }

    public void setOpModFixedPFAbsorbW(Integer opModFixedPFAbsorbW) {
        this.opModFixedPFAbsorbW = opModFixedPFAbsorbW;
    }

    public Integer getOpModFixedPFInjectW() {
        return opModFixedPFInjectW;
    }

    public void setOpModFixedPFInjectW(Integer opModFixedPFInjectW) {
        this.opModFixedPFInjectW = opModFixedPFInjectW;
    }

    public Integer getOpModFixedVar() {
        return opModFixedVar;
    }

    public void setOpModFixedVar(Integer opModFixedVar) {
        this.opModFixedVar = opModFixedVar;
    }

    public Integer getOpModFixedW() {
        return opModFixedW;
    }

    public void setOpModFixedW(Integer opModFixedW) {
        this.opModFixedW = opModFixedW;
    }

    public Integer getOpModFreqDroop() {
        return opModFreqDroop;
    }

    public void setOpModFreqDroop(Integer opModFreqDroop) {
        this.opModFreqDroop = opModFreqDroop;
    }

    public String getOpModFreqWatt() {
        return opModFreqWatt;
    }

    public void setOpModFreqWatt(String opModFreqWatt) {
        this.opModFreqWatt = opModFreqWatt;
    }

    public String getOpModHFRTMayTrip() {
        return opModHFRTMayTrip;
    }

    public void setOpModHFRTMayTrip(String opModHFRTMayTrip) {
        this.opModHFRTMayTrip = opModHFRTMayTrip;
    }

    public String getOpModHFRTMustTrip() {
        return opModHFRTMustTrip;
    }

    public void setOpModHFRTMustTrip(String opModHFRTMustTrip) {
        this.opModHFRTMustTrip = opModHFRTMustTrip;
    }

    public String getOpModHVRTMayTrip() {
        return opModHVRTMayTrip;
    }

    public void setOpModHVRTMayTrip(String opModHVRTMayTrip) {
        this.opModHVRTMayTrip = opModHVRTMayTrip;
    }

    public String getOpModHVRTMomentaryCessation() {
        return opModHVRTMomentaryCessation;
    }

    public void setOpModHVRTMomentaryCessation(String opModHVRTMomentaryCessation) {
        this.opModHVRTMomentaryCessation = opModHVRTMomentaryCessation;
    }

    public String getOpModHVRTMustTrip() {
        return opModHVRTMustTrip;
    }

    public void setOpModHVRTMustTrip(String opModHVRTMustTrip) {
        this.opModHVRTMustTrip = opModHVRTMustTrip;
    }

    public String getOpModLFRTMayTrip() {
        return opModLFRTMayTrip;
    }

    public void setOpModLFRTMayTrip(String opModLFRTMayTrip) {
        this.opModLFRTMayTrip = opModLFRTMayTrip;
    }

    public String getOpModLFRTMustTrip() {
        return opModLFRTMustTrip;
    }

    public void setOpModLFRTMustTrip(String opModLFRTMustTrip) {
        this.opModLFRTMustTrip = opModLFRTMustTrip;
    }

    public String getOpModLVRTMayTrip() {
        return opModLVRTMayTrip;
    }

    public void setOpModLVRTMayTrip(String opModLVRTMayTrip) {
        this.opModLVRTMayTrip = opModLVRTMayTrip;
    }

    public String getOpModLVRTMomentaryCessation() {
        return opModLVRTMomentaryCessation;
    }

    public void setOpModLVRTMomentaryCessation(String opModLVRTMomentaryCessation) {
        this.opModLVRTMomentaryCessation = opModLVRTMomentaryCessation;
    }

    public String getOpModLVRTMustTrip() {
        return opModLVRTMustTrip;
    }

    public void setOpModLVRTMustTrip(String opModLVRTMustTrip) {
        this.opModLVRTMustTrip = opModLVRTMustTrip;
    }

    public Integer getIntegeropModMaxLimW() {
        return IntegeropModMaxLimW;
    }

    public void setIntegeropModMaxLimW(Integer integeropModMaxLimW) {
        IntegeropModMaxLimW = integeropModMaxLimW;
    }

    public Integer getOpModTargetVar() {
        return opModTargetVar;
    }

    public void setOpModTargetVar(Integer opModTargetVar) {
        this.opModTargetVar = opModTargetVar;
    }

    public Integer getOpModTargetW() {
        return opModTargetW;
    }

    public void setOpModTargetW(Integer opModTargetW) {
        this.opModTargetW = opModTargetW;
    }

    public String getOpModVoltVar() {
        return opModVoltVar;
    }

    public void setOpModVoltVar(String opModVoltVar) {
        this.opModVoltVar = opModVoltVar;
    }

    public String getOpModVoltWatt() {
        return opModVoltWatt;
    }

    public void setOpModVoltWatt(String opModVoltWatt) {
        this.opModVoltWatt = opModVoltWatt;
    }

    public String getOpModWattPF() {
        return opModWattPF;
    }

    public void setOpModWattPF(String opModWattPF) {
        this.opModWattPF = opModWattPF;
    }

    public String getOpModWattVar() {
        return opModWattVar;
    }

    public void setOpModWattVar(String opModWattVar) {
        this.opModWattVar = opModWattVar;
    }

    public Integer getRampTms() {
        return rampTms;
    }

    public void setRampTms(Integer rampTms) {
        this.rampTms = rampTms;
    }
}
