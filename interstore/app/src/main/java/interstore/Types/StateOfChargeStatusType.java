package interstore.Types;

public class StateOfChargeStatusType {

    private TimeType dateTime;
    private PerCent value;

    public StateOfChargeStatusType(TimeType dateTime, PerCent value) {
        this.dateTime = dateTime;
        this.value = value;
    }

    public TimeType getDateTime() {
        return dateTime;
    }

    public void setDateTime(TimeType dateTime) {
        this.dateTime = dateTime;
    }

    public PerCent getValue() {
        return value;
    }

    public void setValue(PerCent value) {
        this.value = value;
    }
}
