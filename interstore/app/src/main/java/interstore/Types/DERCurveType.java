package interstore.Types;

import java.util.logging.Logger;

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

    public String toString(DERCurveType value) {
        return String.valueOf(value);
    }

}
