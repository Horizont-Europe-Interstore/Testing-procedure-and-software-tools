package interstore.Types;

public class TimeType extends Int64{
    private long value;

    public TimeType(long value) {
        super(value);
        this.value = value;
    }
    public long getValue(){
        return this.value;
    }
    public void setValue(long value){
        this.value = value;
    }

}

