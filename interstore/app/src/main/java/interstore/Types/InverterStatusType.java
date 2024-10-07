package interstore.Types;
/*
    DER InverterStatus value:
    0 - N/A
    1 - off
    2 - sleeping (auto-shutdown) or DER is at low output power/voltage
    3 - starting up or ON but not producing power
    4 - tracking MPPT power point
    5 - forced power reduction/derating
    6 - shutting down
    7 - one or more faults exist
    8 - standby (service on unit) - DER may be at high output voltage/power
    9 - test mode
    10 - as defined in manufacturer status
    All other values reserved.
*/

public class InverterStatusType {
    private TimeType dateTime;
    private UInt8 value;

    public InverterStatusType(TimeType dateTime, short value) {
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
        if (value < 0 || value > 10) {
            throw new IllegalArgumentException("Invalid InverterStatusType value: " + value + ". Value must be within the valid range of 0 - 10");
        }
        this.value = new UInt8(value);
    }
}
