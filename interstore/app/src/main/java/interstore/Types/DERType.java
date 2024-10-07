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


    public String toString(short value) {
        return String.valueOf(value);
    }
}
