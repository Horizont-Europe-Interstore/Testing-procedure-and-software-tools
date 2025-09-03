package interstore.Types;

import java.math.BigInteger;

public class HexBinary128 {
    private String hexValue128Value;

    public HexBinary128(String hexValue) {
        this.hexValue128Value = validateAndFormatHexValue(hexValue);
    }

    public String getHexValue128Value() {
        return hexValue128Value;
    }

    public void setHexValue128Value(String hexValue) {
        this.hexValue128Value = validateAndFormatHexValue(hexValue);
    }

    public static String validateAndFormatHexValue(String hexValue) {
        if (!isValidHexBinary128(hexValue)) {
            throw new IllegalArgumentException("Invalid HexBinary128 value: " + hexValue);
        }
        return hexValue.length() % 2 == 0 ? hexValue : "0" + hexValue;
    }

    private static boolean isValidHexBinary128(String hexValue) {
        try {
            new BigInteger(hexValue, 16);
        } catch (NumberFormatException e) {
            return false;
        }
        return hexValue.length() <= 32;
    }
}
