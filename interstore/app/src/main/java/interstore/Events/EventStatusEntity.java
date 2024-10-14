package interstore.Events;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Event_Status")
public class EventStatusEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currentStatus")
    public Integer currentStatus;

    @Column(name = "dateTime")
    public String dateTime;

    @Column(name = "potentiallySuperseded")
    public boolean potentiallySuperseded;



    public EventStatusEntity() {
    }

    public Long getId() {
        return id;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isPotentiallySuperseded() {
        return potentiallySuperseded;
    }

    public void setPotentiallySuperseded(boolean potentiallySuperseded) {
        this.potentiallySuperseded = potentiallySuperseded;
    }
}
