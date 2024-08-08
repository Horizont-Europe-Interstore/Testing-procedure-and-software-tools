package interstore.Types;

import java.math.BigInteger;

public class HexBinary128 {
    private String hexValue;

    public HexBinary128(String hexValue) {
        this.hexValue = validateAndFormatHexValue(hexValue);
    }

    public String getHexValue() {
        return hexValue;
    }

    public void setHexValue(String hexValue) {
        this.hexValue = validateAndFormatHexValue(hexValue);
    }

    private String validateAndFormatHexValue(String hexValue) {
        if (!isValidHexBinary128(hexValue)) {
            throw new IllegalArgumentException("Invalid HexBinary128 value: " + hexValue);
        }
        return hexValue.length() % 2 == 0 ? hexValue : "0" + hexValue;
    }

    private boolean isValidHexBinary128(String hexValue) {
        try {
            new BigInteger(hexValue, 16);
        } catch (NumberFormatException e) {
            return false;
        }
        return hexValue.length() <= 32;
    }
}
