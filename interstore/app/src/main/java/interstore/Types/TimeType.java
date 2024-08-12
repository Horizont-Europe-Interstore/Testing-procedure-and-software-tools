package interstore.Types;

public class TimeType extends Int64{
    private long Int64Value;

    public TimeType(long value) {
        super(value);
        this.Int64Value = value;
    }
    public long getInt64Value(){
        return this.Int64Value;
    }
    public void setInt64Value(long value){
        this.Int64Value = value;
    }

}

