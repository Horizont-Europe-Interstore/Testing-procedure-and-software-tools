package interstore.Time;
import interstore.Identity.Link;
import interstore.Types.TimeType; 
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.ZoneId;
public class TimeDto {

     private TimeType currentTimeInstance ;
     private Link theLink;
     private static String TimeLink;  
     // below attributes shall be implimented depends om the priority . 
    // private TimeType dstEndTime; time at which daylight savings ends 
    // private TimeType dstStartTime; time at which daylight savings starts
    // private TimeOffSetType dstOffset: Daylight savings time offset from local standard time. A typical practice is advancing clocks one hour
    //when daylight savings time is in effect, which would result in a positive dstOffset.
    // private TimeType localTime; Local time: localTime = currentTime + tzOffset (+ dstOffset when in effect).
    // private TimeOffSetType tzOffset; Time zone offset from UTC. Local time zone offset from currentTime. Does not include any daylight savings time offsets. For American
    // time zones, a negative tzOffset SHALL be used (e.g., EST = GMT −5 which is −18 000).
    
    public TimeDto() {
        Instant instant = Instant.now();
        long currentTime =  instant.getEpochSecond();
        this.currentTimeInstance = new TimeType(currentTime);
       
    }


    // the time link has to give the output of the resource that has created . 
   // public static String getTimeLink() {
     // return Link.produceLink("/tm"); 

    //}


    public long getCurrentTime() {
        return currentTimeInstance.getValue();
    }

    
    public void setCurrentTime(long currentTime) {
          this.currentTimeInstance.setValue(currentTime);
    }
        

    public String toHumanReadableFormat() {
        Instant instant = Instant.ofEpochSecond(getCurrentTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                                        .withZone(ZoneId.of("UTC"));
        return formatter.format(instant);
    }


}

