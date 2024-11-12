package interstore.Events;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class RandomizableEventEntity extends EventEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "randomizeDuration")
    private Integer randomizeDuration;

    @Column(name = "randomizeStart")
    private Integer randomizeStart;

    public RandomizableEventEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public Integer getRandomizeDuration() {
        return randomizeDuration;
    }

    public void setRandomizeDuration(Integer randomizeDuration) {
        this.randomizeDuration = randomizeDuration;
    }

    public Integer getRandomizeStart() {
        return randomizeStart;
    }

    public void setRandomizeStart(Integer randomizeStart) {
        this.randomizeStart = randomizeStart;
    }
}
