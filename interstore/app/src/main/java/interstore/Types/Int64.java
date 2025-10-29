package interstore.Types;

public class Int64 {
    private final long Int64Value;

    public Int64(long value) {
        validateRange(value);
        this.Int64Value = value;
    }

    private void validateRange(long value) {
        if (value < Long.MIN_VALUE || value > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Value must be between -9223372036854775808 and 9223372036854775807");
        }
    }

//    public long getValue() {
//        return value;
//    }

//    @Override
//    public String toString() {
//        return Long.toString(value);
//    }

    // Add additional methods, if needed
}
