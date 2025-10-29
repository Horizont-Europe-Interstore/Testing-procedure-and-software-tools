package interstore.Types;

public class UInt16 {
    private final int UintValue;

    public UInt16(int value) {
        if (value < 0 || value > 65535) {
            throw new IllegalArgumentException("Value must be between 0 and 65535");
        }
        this.UintValue = value;
    }

    public int getUintValue() {
        return UintValue;
    }

    @Override
    public String toString() {
        return Integer.toString(UintValue);
    }
}

