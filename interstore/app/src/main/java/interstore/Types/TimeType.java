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

    // Static method to convert from long to TimeType
    public static TimeType fromLong(long value) {
        return new TimeType(value);
    }

    // Static method to convert from TimeType to long
    public static long toLong(TimeType timeType) {
        if (timeType == null) {
            throw new IllegalArgumentException("TimeType cannot be null");
        }
        return timeType.getInt64Value();
    }

}

