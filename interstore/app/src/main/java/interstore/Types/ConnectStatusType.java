package interstore.Types;

/*
    DER ConnectStatus value (bitmap):
    0 - Connected
    1 - Available
    2 - Operating
    3 - Test
    4 - Fault / Error
All other values reserved.
*/

public class ConnectStatusType {

    private TimeType dateTime;
    private HexBinary8 value;

    public ConnectStatusType(TimeType dateTime, String value) {
        this.dateTime = dateTime;
        this.setValue(value);
    }

    // Getter for dateTime
    public TimeType getDateTime() {
        return dateTime;
    }

    // Getter for value
    public HexBinary8 getValue() {
        return value;
    }

    // Setter for dateTime
    public void setDateTime(TimeType dateTime) {
        this.dateTime = dateTime;
    }

    // Setter for value with validation
    public void setValue(String value) {
        HexBinary8 new_val = new HexBinary8(value);

        if (new_val.isValidHexBinary8(value)) {
            if (new_val.toInteger() < 0 || new_val.toInteger() > 4) {
                throw new IllegalArgumentException("Invalid ConnectStatusType value: " + value + ". Value must be within the valid range of 0 - 4");
            }
        } else {
            throw new IllegalArgumentException("Invalid HexBinary8 format: " + value);
        }

        this.value = new_val;
    }

}
