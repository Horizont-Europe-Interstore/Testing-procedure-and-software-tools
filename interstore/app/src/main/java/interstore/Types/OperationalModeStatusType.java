package interstore.Types;

/*
    DER OperationalModeStatus value:
    0 - Not applicable / Unknown
    1 - Off
    2 - Operational mode
    3 - Test mode
    All other values reserved.
*/

public class OperationalModeStatusType {
    private TimeType dateTime;
    private UInt8 value;

    public OperationalModeStatusType(TimeType dateTime, short value) {
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
        if (value < 0 || value > 3) {
            throw new IllegalArgumentException("Invalid OperationalModeStatusType value: " + value + ". Value must be within the valid range of 0 - 3");
        }
        this.value = new UInt8(value);
    }

}
