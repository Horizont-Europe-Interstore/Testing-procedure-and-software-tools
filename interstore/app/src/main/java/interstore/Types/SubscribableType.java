package interstore.Types;
public class SubscribableType extends UInt8 {
    private short SubscribabaleTypevalue;
    public SubscribableType(short value) {
        super(value);
        if (value < 0 || value > 3) {
            throw new IllegalArgumentException("Value must be between 0 and 3");
        }
        this.SubscribabaleTypevalue = value;
    }

    @Override
    public short getUint8Value() {
        return super.getUint8Value();
    }
    public String getString(){
        return String.valueOf(this.SubscribabaleTypevalue);
    }
}
