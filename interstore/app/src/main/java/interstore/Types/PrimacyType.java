package interstore.Types;

public class PrimacyType extends UInt8 {
    private short value;
    public PrimacyType(short value) {
        super(value);
        if (value >= 3 && value <= 64) {
            throw new IllegalArgumentException("Values 3 to 64 are reserved");
        }
        if (value >= 192 && value <= 255) {
            throw new IllegalArgumentException("Values 192 to 255 are reserved");
        }
        this.value = value;
    }

    public String getString(){
        return String.valueOf(this.value);
    }
}
