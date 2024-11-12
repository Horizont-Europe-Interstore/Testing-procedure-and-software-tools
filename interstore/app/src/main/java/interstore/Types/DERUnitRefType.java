package interstore.Types;

import java.util.logging.Logger;

public class DERUnitRefType extends UInt8{
    private static final Logger LOGGER = Logger.getLogger(DERUnitRefType.class.getName());

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
}
