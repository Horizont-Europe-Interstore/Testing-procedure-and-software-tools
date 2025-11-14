package interstore.Time;

import interstore.Types.UInt8;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String currentTimeInstance ;
    private String timeLink;
    // below attributes shall be implimented depends om the priority .
    // private TimeType dstEndTime; time at which daylight savings ends
    // private TimeType dstStartTime; time at which daylight savings starts
    // private TimeOffSetType dstOffset: Daylight savings time offset from local standard time. A typical practice is advancing clocks one hour
    //when daylight savings time is in effect, which would result in a positive dstOffset.
    // private TimeType localTime; Local time: localTime = currentTime + tzOffset (+ dstOffset when in effect).
    // private TimeOffSetType tzOffset; Time zone offset from UTC. Local time zone offset from currentTime. Does not include any daylight savings time offsets. For American
    // time zones, a negative tzOffset SHALL be used (e.g., EST = GMT −5 which is −18 000).
     public String quality;
    public TimeEntity() {
        Instant instant = Instant.now();
        long currentTime =  instant.getEpochSecond();
        this.currentTimeInstance = String.valueOf(currentTime);
        this.quality = new UInt8((short) 7).toString();
    }


    // the time link has to give the output of the resource that has created . 
   // public static String getTimeLink() {
     // return Link.produceLink("/tm"); 

    //}


    public String getCurrentTime() {
        return currentTimeInstance;
    }


    public void setCurrentTime(String currentTime) {
        this.currentTimeInstance = currentTime;
    }

    public String getQuality() {
        return this.quality;
    }

    public Long getId() {
        return id;
    }

    public String getTimeLink() {
        return timeLink;
    }

    public void setTimeLink(String timeLink) {
        this.timeLink = timeLink;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }



 /*   public String toHumanReadableFormat() {
        Instant instant = Instant.ofEpochSecond(Long.parseLong(getCurrentTime()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("UTC"));
        return formatter.format(instant);
    }*/


}

