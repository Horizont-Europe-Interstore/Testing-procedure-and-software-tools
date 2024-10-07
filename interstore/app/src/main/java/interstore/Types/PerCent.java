package interstore.Types;

public class PerCent extends UInt16{

    // Constructor for PerCent that validates the percentage range
    public PerCent(int value) {
        // Call the superclass constructor (UInt16) with the value
        super(validatePercent(value));
    }

    // Method to validate that the percentage is between 0 and 10,000
    private static int validatePercent(int value) {
        if (value < 0 || value > 10000) {
            throw new IllegalArgumentException("Percentage value must be between 0 and 10000 (where 10000 represents 100%)");
        }
        return value;
    }

    // Override toString for a more readable percentage representation
    @Override
    public String toString() {
        // Return the percentage value as a string in hundredths of a percent
        return getUintValue() + " (hundredths of a percent)";
    }

    // A helper method to convert the value to a percentage (0 to 100 range)
    public double toPercentage() {
        return getUintValue() / 100.0;
    }
}
