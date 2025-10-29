package interstore.Types;

public class String32 {
    private final String String32value;

    public String32(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value.length() > 32) {
            throw new IllegalArgumentException("Value length must not exceed 32 characters");
        }
        this.String32value = value;
    }

    public String getString32value() {
        return String32value;
    }

    @Override
    public String toString() {
        return String32value;
    }
}
