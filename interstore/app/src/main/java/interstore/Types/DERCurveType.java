package interstore.Types;

import java.util.logging.Logger;
/*
 * 0 - opModFreqWatt (Frequency-Watt Curve Mode)
1 - opModHFRTMayTrip (High Frequency Ride Through, May Trip Mode)
2 - opModHFRTMustTrip (High Frequency Ride Through, Must Trip Mode)
3 - opModHVRTMayTrip (High Voltage Ride Through, May Trip Mode)
4 - opModHVRTMomentaryCessation (High Voltage Ride Through, Momentary Cessation
Mode)
5 - opModHVRTMustTrip (High Voltage Ride Through, Must Trip Mode)
6 - opModLFRTMayTrip (Low Frequency Ride Through, May Trip Mode)
7 - opModLFRTMustTrip (Low Frequency Ride Through, Must Trip Mode)
8 - opModLVRTMayTrip (Low Voltage Ride Through, May Trip Mode)
9 - opModLVRTMomentaryCessation (Low Voltage Ride Through, Momentary Cessation
Mode)
10 - opModLVRTMustTrip (Low Voltage Ride Through, Must Trip Mode)
11 - opModVoltVar (Volt-Var Mode)
12 - opModVoltWatt (Volt-Watt Mode)
13 - opModWattPF (Watt-PowerFactor Mode)
14 - opModWattVar (Watt-Var Mode)
 */

public class DERCurveType extends UInt8{
    private static final Logger LOGGER = Logger.getLogger(DERCurveType.class.getName());

    // Constructor ensuring the value is between 0 and 14
    public DERCurveType(short value) {
        super(value);
        if (!isValidDERCurveType(value)) {
            throw new IllegalArgumentException("Value must be between 0 and 14");
        }
    }

    // Static method to create a DERCurveType from a string, with validation
    public static DERCurveType fromString(String value) {
        short val = Short.parseShort(value);
        if (!isValidDERCurveType(val)) {
            throw new IllegalArgumentException("Value must be between 0 and 14");
        }
        return new DERCurveType(val);
    }

    // Static method for checking if a short value is valid (0 to 14)
    public static boolean isValidDERCurveType(short value) {
        return value >= 0 && value <= 14;
    }
    
    public String getDescription() {
        switch (getUint8Value()) {
            case 0: return "0 - Frequency-Watt Curve Mode";
            case 1: return "1 - High Frequency Ride Through, May Trip Mode";
            case 2: return "2 - High Frequency Ride Through, Must Trip Mode";
            case 3: return "3 - High Voltage Ride Through, May Trip Mode";
            case 4: return "4 - High Voltage Ride Through, Momentary Cessation Mode";
            case 5: return "5 - High Voltage Ride Through, Must Trip Mode";
            case 6: return "6 - Low Frequency Ride Through, May Trip Mode";
            case 7: return "7 - Low Frequency Ride Through, Must Trip Mode";
            case 8: return "8 - Low Voltage Ride Through, May Trip Mode";
            case 9: return "9 - Low Voltage Ride Through, Momentary Cessation Mode";
            case 10: return "10 - Low Voltage Ride Through, Must Trip Mode";
            case 11: return "11 - Volt-Var Mode";
            case 12: return "12 - Volt-Watt Mode";
            case 13: return "13 - Watt-PowerFactor Mode";
            case 14: return "14 - Watt-Var Mode";
            default: return getUint8Value() + " - Unknown DER Curve Type";
        }
    }

    public String toString(DERCurveType value) {
        return String.valueOf(value);
    }

}
