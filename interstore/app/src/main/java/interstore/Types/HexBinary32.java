package interstore.Types;


public class HexBinary32 {
    private  String hexValue;

    

    public String getHexValue() {
        return hexValue;
    }
    
    public void setHexValue(String hexValue) {
        // Ensure that the hex string is valid
        if (!isValidHexBinary32(hexValue)) {
            throw new IllegalArgumentException("Invalid HexBinary32 value: " + hexValue);
        }

     
        this.hexValue = hexValue.length() % 2 == 0 ? hexValue : "0" + hexValue;
    }


    // Check if the given hex string is a valid HexBinary32 value
    private boolean isValidHexBinary32(String hexValue) {
        // Check if it is a valid hex string and has at most 8 characters
        try {
            Integer.parseInt(hexValue,16);
        }
        catch (NumberFormatException e){
            return false;
        }
        return hexValue.length() <= 8;
    }
}


/*
 * public HexBinary32(String hexValue) {
        // Ensure that the hex string is valid
        if (!isValidHexBinary32(hexValue)) {
            throw new IllegalArgumentException("Invalid HexBinary32 value: " + hexValue);
        }

        // Store the hex string (with a leading "0" if necessary)
        this.hexValue = hexValue.length() % 2 == 0 ? hexValue : "0" + hexValue;
    }
 * 
 * 
 */