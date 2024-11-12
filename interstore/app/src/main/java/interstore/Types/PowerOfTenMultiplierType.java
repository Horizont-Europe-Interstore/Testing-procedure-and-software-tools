package interstore.Types;

import java.util.logging.Logger;

public class PowerOfTenMultiplierType extends Int8{
    private static final Logger LOGGER = Logger.getLogger(PowerOfTenMultiplierType.class.getName());

    // Constructor, ensuring the value is between -9 and 9
    public PowerOfTenMultiplierType(int value) {
        super(value);
        if (!isValidPowerOfTenMultiplier(value)) {
            throw new IllegalArgumentException("Value must be between -9 and 9");
        }
    }

    // Static method to check if a value is a valid Power of Ten Multiplier (-9 to 9)
    public static boolean isValidPowerOfTenMultiplier(int value) {
        return value >= -9 && value <= 9;
    }

    // Static method to create PowerOfTenMultiplierType from string, with validation
    public static PowerOfTenMultiplierType fromString(String value) {
        short val = Short.parseShort(value);
        if (!isValidPowerOfTenMultiplier(val)) {
            throw new IllegalArgumentException("Invalid PowerOfTenMultiplier value: " + value);
        }
        return new PowerOfTenMultiplierType(val);
    }

    // Overriding toString() for better readability
    @Override
    public String toString() {
        return "Value: " + getValue();
    }
}
