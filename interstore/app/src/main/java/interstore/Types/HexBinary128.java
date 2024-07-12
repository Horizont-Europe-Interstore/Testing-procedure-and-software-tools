package interstore.Types;

import java.math.BigInteger;

public class HexBinary128 {
    private String hexValue;

    public HexBinary128(String hexValue) {

        if (!isValidHexBinary128(hexValue)) {
            throw new IllegalArgumentException("Invalid HexBinary128 value: " + hexValue);
        }


        this.hexValue = hexValue.length() % 2 == 0 ? hexValue : "0" + hexValue;
    }


    public String getHexValue() {
        return hexValue;
    }


    private boolean isValidHexBinary128(String hexValue) {

        try {
            BigInteger value = new BigInteger(hexValue, 16);
//            Long.parseLong(hexValue, 16);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return hexValue.length() <= 32;
    }
}
