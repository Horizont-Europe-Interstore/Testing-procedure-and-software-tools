package interstore.Types;

public class SubscribableType extends UInt8 {
    private short value;
    public SubscribableType(short value) {
        super(value);
        if (value < 0 || value > 3) {
            throw new IllegalArgumentException("Value must be between 0 and 3");
        }
        this.value = value;
    }

    @Override
    public short getValue() {
        return super.getValue();
    }
    public String getString(){
        return String.valueOf(this.value);
    }
}
