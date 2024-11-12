package interstore.Types;

/*
    DER StorageModeStatus value:
    0 – storage charging
    1 – storage discharging
    2 – storage holding
    All other values reserved.
*/

public class StorageModeStatusType {

    private TimeType dateTime;
    private UInt8 value;

    public StorageModeStatusType(TimeType dateTime, short value) {
        this.dateTime = dateTime;
        this.setValue(value);
    }

    // Getter for dateTime
    public TimeType getDateTime() {
        return dateTime;
    }

    // Setter for dateTime
    public void setDateTime(TimeType dateTime) {
        this.dateTime = dateTime;
    }

    // Getter for value
    public UInt8 getValue() {
        return value;
    }

    // Setter for value with validation
    public void setValue(short value) {
        if (value < 0 || value > 2) {
            throw new IllegalArgumentException("Invalid StorageModeStatusType value: " + value + ". Value must be within the valid range of 0 - 2");
        }
        this.value = new UInt8(value);
    }
}
