package interstore.Types;

public class UInt16 {
    private final int value;

    public UInt16(int value) {
        if (value < 0 || value > 65535) {
            throw new IllegalArgumentException("Value must be between 0 and 65535");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

