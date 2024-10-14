package interstore.Events;
import interstore.Identity.RespondableSubscribableIdentifiedObjectEntity;
import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "Event")
public class EventEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "creationTime")
    public String creationTime;

    @Column(name = "timeInterval")
    public Integer timeInterval;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "respondable_subscribable_identified_object")
    private RespondableSubscribableIdentifiedObjectEntity respondableSubscribableIdentifiedObjectEntity;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_status")
    private EventStatusEntity eventStatusEntity;


    public EventEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }

    public RespondableSubscribableIdentifiedObjectEntity getRespondableSubscribableIdentifiedObjectEntity() {
        return respondableSubscribableIdentifiedObjectEntity;
    }

    public void setRespondableSubscribableIdentifiedObjectEntity(RespondableSubscribableIdentifiedObjectEntity respondableSubscribableIdentifiedObjectEntity) {
        this.respondableSubscribableIdentifiedObjectEntity = respondableSubscribableIdentifiedObjectEntity;
    }

    public EventStatusEntity getEventStatusEntity() {
        return eventStatusEntity;
    }

    public void setEventStatusEntity(EventStatusEntity eventStatusEntity) {
        this.eventStatusEntity = eventStatusEntity;
    }
}
