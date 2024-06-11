package interstore.Types;

import java.util.logging.Logger;

public class UInt8 {
    private final short value;
    private static final Logger LOGGER = Logger.getLogger(UInt8.class.getName());

    public UInt8(short value) {
        if (value < 0 || value > 0xFF) {
            throw new IllegalArgumentException("Value must be between 0 and 255");
        }
        this.value = value;
    }

    public short getValue() {
        return this.value;
    }

    // Add methods for arithmetic operations, comparison, etc., as needed

    @Override
    public String toString() {
        return Short.toString(value);
    }

    public static UInt8 toUInt8(String value) {
        short val = isValidUInt8(value);
        return new UInt8(val);
    }

    public static short isValidUInt8(String value) {
        try {
            short val = Short.parseShort(value);
            if (val < 0 || val > 0xFF) {
                throw new IllegalArgumentException("Value must be between 0 and 255");
            }
            return val;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid UInt8 value: " + value);
        }
    }
}