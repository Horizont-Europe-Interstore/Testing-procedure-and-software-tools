package interstore.Types;

public class HexBinary8 {
    private String hexValue8Value;

    // Constructor that initializes the value with validation and formatting
    public HexBinary8(String hexValue) {
        this.hexValue8Value = validateAndFormatHexValue(hexValue);
    }

    // Getter for the hex value
    public String getHexValue8Value() {
        return hexValue8Value;
    }

    // Setter that validates and sets the hex value
    public void setHexValue8Value(String hexValue) {
        this.hexValue8Value = validateAndFormatHexValue(hexValue);
    }

    // Validation and formatting function
    private String validateAndFormatHexValue(String hexValue) {
        // Ensure the value is a valid hex string of at most 2 characters (8 bits)
        if (!isValidHexBinary8(hexValue)) {
            throw new IllegalArgumentException("Invalid HexBinary8 value: " + hexValue);
        }

        // Ensure that the hex string has an even number of characters by prepending a "0" if necessary
        return hexValue.length() % 2 == 0 ? hexValue : "0" + hexValue;
    }

    // Check if the given hex string is a valid HexBinary8 value
    public boolean isValidHexBinary8(String hexValue) {
        try {
            // Parse the hex string to ensure it's a valid hex number
            Integer.parseInt(hexValue, 16);
        } catch (NumberFormatException e) {
            return false;
        }

        // Ensure that the hex string has at most 2 characters (8 bits)
        return hexValue.length() <= 2;
    }

    // Method to convert the hex value to an integer
    public int toInteger() {
        return Integer.parseInt(hexValue8Value, 16);
    }
}
