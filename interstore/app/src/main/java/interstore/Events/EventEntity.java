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

    @Column(name = "duration")
    public Integer duration;

    @Column(name = "start")
    public Integer start;

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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
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
