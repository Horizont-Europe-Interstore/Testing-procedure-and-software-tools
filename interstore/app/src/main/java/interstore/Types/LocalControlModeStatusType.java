package interstore.Types;

/*
    DER LocalControlModeStatus/value:
    0 – local control
    1 – remote control
    All other values reserved.
*/

public class LocalControlModeStatusType {
    private TimeType dateTime;
    private UInt8 value;

    public LocalControlModeStatusType(TimeType dateTime, short value) {
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
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Invalid LocalControlModeStatusType value: " + value + ". Value must be either 0 or 1");
        }
        this.value = new UInt8(value);
    }
}
