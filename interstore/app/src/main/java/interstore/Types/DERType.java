package interstore.Types;

import java.util.logging.Logger;
/*
* 0 - Not applicable / Unknown
* 1 - Virtual or mixed DER
* 2 - Reciprocating engine
* 3 - Fuel cell
* 4 - Photovoltaic system
* 5 - Combined heat and power
* 6 - Other generation system
* 80 - Other storage system
* 81 - Electric vehicle
* 82 - EVSE
* 83 - Combined PV and storage
* All other values reserved.
*/

public class DERType extends UInt8{
    private static final Logger LOGGER = Logger.getLogger(DERType.class.getName());
    public static final short NOT_APPLICABLE = 0;
    public static final short VIRTUAL_OR_MIXED = 1;
    public static final short RECIPROCATING_ENGINE = 2;
    public static final short FUEL_CELL = 3;
    public static final short PHOTOVOLTAIC_SYSTEM = 4;
    public static final short COMBINED_HEAT_AND_POWER = 5;
    public static final short OTHER_GENERATION_SYSTEM = 6;
    public static final short OTHER_STORAGE_SYSTEM = 80;
    public static final short ELECTRIC_VEHICLE = 81;
    public static final short EVSE = 82;
    public static final short COMBINED_PV_AND_STORAGE = 83;

    // Constructor that accepts a short value and validates it
    public DERType(short value) {
        super(validateDERType(value));
    }

    // Constructor that accepts a string and converts it to UInt8
    public DERType(String value) {
        super(validateDERType(toUInt8(value).getUint8Value()));
    }

    // Static method to validate if the value is within the valid range
    private static short validateDERType(short value) {
        if (!((value >=0 && value <= 6) || (value >=80 && value <= 83))){
            throw new IllegalArgumentException("Invalid DERType value: " + value + ". Value must be within the valid range.");
        }
        else{
            return value;
        }
    }
    
    public String getDescription() {
        switch (getUint8Value()) {
            case NOT_APPLICABLE: return "Not applicable / Unknown";
            case VIRTUAL_OR_MIXED: return "Virtual or mixed DER";
            case RECIPROCATING_ENGINE: return "Reciprocating engine";
            case FUEL_CELL: return "Fuel cell";
            case PHOTOVOLTAIC_SYSTEM: return "Photovoltaic system";
            case COMBINED_HEAT_AND_POWER: return "Combined heat and power";
            case OTHER_GENERATION_SYSTEM: return "Other generation system";
            case OTHER_STORAGE_SYSTEM: return "Other storage system";
            case ELECTRIC_VEHICLE: return "Electric vehicle";
            case EVSE: return "EVSE";
            case COMBINED_PV_AND_STORAGE: return "Combined PV and storage";
            default: return "Unknown DER Type";
        }
    }

    public String toString(short value) {
        return String.valueOf(value);
    }
}
