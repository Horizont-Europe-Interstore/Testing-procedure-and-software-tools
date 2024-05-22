package interstore.Types;
import java.util.logging.Logger; 
public class UInt32 {
    private final long value;
    private static final Logger LOGGER = Logger.getLogger(UInt32.class.getName());

    public UInt32(long value) {
        if (value < 0 || value > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("Value must be between 0 and 4294967295");
        }
        this.value = value;
        
    }

    public long getValue() {
       // LOGGER.info("value from UInt 32  " + this.value);
        return this.value;
    }

    // Add methods for arithmetic operations, comparison, etc., as needed

    @Override
    public String toString() {
        return Long.toString(value);
    }

    public static UInt32 toUInt32(String value){
        long val = isValidUInt32(value);
        return new UInt32(val);
    }

    public static long isValidUInt32(String value){
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid UInt32 value: " + value);
        }
    }
}
