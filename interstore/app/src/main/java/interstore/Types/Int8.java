package interstore.Types;

public class Int8 {
    private byte value;

    // Constructor
    public Int8(int value) {
        // Ensure the value is within -128 and 127
        if (value < -128 || value > 127) {
            throw new IllegalArgumentException("Value must be between -128 and 127");
        }
        this.value = (byte) value;
    }

    // Getter method
    public byte getValue() {
        return value;
    }

    // Setter method
    public void setValue(int value) {
        if (value < -128 || value > 127) {
            throw new IllegalArgumentException("Value must be between -128 and 127");
        }
        this.value = (byte) value;
    }

    @Override
    public String toString() {
        return Byte.toString(value);
    }
}
