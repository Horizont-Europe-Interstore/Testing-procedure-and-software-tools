package interstore.Types;

public class String32 {
    private final String value;

    public String32(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value.length() > 32) {
            throw new IllegalArgumentException("Value length must not exceed 32 characters");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
