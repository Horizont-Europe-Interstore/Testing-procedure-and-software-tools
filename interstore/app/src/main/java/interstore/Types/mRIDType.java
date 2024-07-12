package interstore.Types;

import java.math.BigInteger;

public class mRIDType extends HexBinary128 {
//    private final BigInteger PEN;

    public mRIDType(String hexValue) {
        super(hexValue);
        try {
            BigInteger value = new BigInteger(hexValue, 16);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid mRID value: " + hexValue);
        }
        BigInteger value = new BigInteger(hexValue, 16);
        // Extract the PEN from the least significant bits
//        int startIndex = value.bitLength() - 8;
//        this.PEN = new BigInteger(value.toString().substring(startIndex)); // Assuming PEN is the last 8 characters
    }

//    public BigInteger getPEN() {
//        return PEN;
//    }

    @Override
    public String getHexValue() {
        return super.getHexValue();
    }
}
