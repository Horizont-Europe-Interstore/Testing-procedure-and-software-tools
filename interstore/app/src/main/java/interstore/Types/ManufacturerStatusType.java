package interstore.Types;

/*
    DER ManufacturerStatus/value:
    String data type
*/

public class ManufacturerStatusType {

    private TimeType dateTime;
    private String value;

    public ManufacturerStatusType(TimeType dateTime, String value) {
        this.dateTime = dateTime;
        this.value = value;
    }

    public TimeType getDateTime() {
        return dateTime;
    }

    public void setDateTime(TimeType dateTime) {
        this.dateTime = dateTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
