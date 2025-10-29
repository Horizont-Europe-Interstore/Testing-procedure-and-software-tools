package interstore.Types;

import java.util.logging.Logger;

public class DERUnitRefType extends UInt8{
    private static final Logger LOGGER = Logger.getLogger(DERUnitRefType.class.getName());
   
    private short percentageSetMaxW;
    private short percentageSetMaxVar;
    private short percentageStatVarAvail;
    private short percentageSetEffectiveV;
    private short percentageSetMaxChargeRateW;
    private short percentageSetMaxDischargeRateW;
    private short percentageStatWAvail;

    // Constructor ensuring that the value is between 0 and 7
    public DERUnitRefType(short value) {
        super(value);
        if (!isValidDERUnitRefType(value)) {
            throw new IllegalArgumentException("Value must be between 0 and 7");
        }
    }

    // Static method to check if a value is a valid DERUnitRefType (0 to 7)
    public static boolean isValidDERUnitRefType(short value) {
        return value >= 0 && value <= 7;
    }

    // Static method to create a DERUnitRefType from a string, with validation
    public static DERUnitRefType fromString(String value) {
        short val = Short.parseShort(value);
        if (!isValidDERUnitRefType(val)) {
            throw new IllegalArgumentException("Invalid DERUnitRefType value: " + value);
        }
        return new DERUnitRefType(val);
    }

    // Overriding toString for better representation
    public String toString(DERUnitRefType value) {
        return String.valueOf(value);
    }
    /*write setters and getters for the private members and the 
     * setters and getters has to vheck that there are UInt so the 
     * extends super class to be called 
     */

     public short getPercentageSetMaxW() {
        return percentageSetMaxW;
    }
    public void setPercentageSetMaxW(String percentageSetMaxW) {
        this.percentageSetMaxW =  UInt8.isValidUInt8(percentageSetMaxW);
    }
    public short getPercentageSetMaxVar() {
        return percentageSetMaxVar;
    }
    public void setPercentageSetMaxVar(String percentageSetMaxVar) {
        this.percentageSetMaxVar = UInt8.isValidUInt8(percentageSetMaxVar);
    }
    public short getPercentageStatVarAvail() {
        return percentageStatVarAvail;
    }
    public void setPercentageStatVarAvail(String percentageStatVarAvail) {
        this.percentageStatVarAvail = UInt8.isValidUInt8(percentageStatVarAvail);
    }
    public short getPercentageSetEffectiveV() {
        return percentageSetEffectiveV;
    }
    public void setPercentageSetEffectiveV(String percentageSetEffectiveV) {
        this.percentageSetEffectiveV = UInt8.isValidUInt8(percentageSetEffectiveV);
    }
    public short getPercentageSetMaxChargeRateW() {
        return percentageSetMaxChargeRateW;
    }
    public void setPercentageSetMaxChargeRateW(String percentageSetMaxChargeRateW) {
        this.percentageSetMaxChargeRateW = UInt8.isValidUInt8(percentageSetMaxChargeRateW);
    }
    public short getPercentageSetMaxDischargeRateW() {
        return percentageSetMaxDischargeRateW;
    }
    public void setPercentageSetMaxDischargeRateW(String percentageSetMaxDischargeRateW) {
        this.percentageSetMaxDischargeRateW = UInt8.isValidUInt8(percentageSetMaxDischargeRateW);
    }
    public short getPercentageStatWAvail() {
        return percentageStatWAvail;
    }
    public void setPercentageStatWAvail(String percentageStatWAvail) {
        this.percentageStatWAvail = UInt8.isValidUInt8(percentageStatWAvail);
    }

}
