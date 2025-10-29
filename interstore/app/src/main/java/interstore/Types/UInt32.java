package interstore.Types;
import java.util.logging.Logger; 
public class UInt32 {
    private final long UInt32Value;
    private static final Logger LOGGER = Logger.getLogger(UInt32.class.getName());

    public UInt32(long value) {
        if (value < 0 || value > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("Value must be between 0 and 4294967295");
        }
        this.UInt32Value = value;
        
    }

    public long getUInt32Value() {
       // LOGGER.info("value from UInt 32  " + this.value);
        return this.UInt32Value;
    }

    // Add methods for arithmetic operations, comparison, etc., as needed

    @Override
    public String toString() {
        return Long.toString(UInt32Value);
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
