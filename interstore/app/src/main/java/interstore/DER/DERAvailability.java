package interstore.DER;

import interstore.Identity.SubscribableResource;
import jakarta.persistence.*;

@Entity
public class DERAvailability extends SubscribableResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "derAvailability")
    private DERDto derDto;
    @Column(name = "availabilityDuration")
    private int availabilityDuration;

    @Column(name = "maxChargeDuration")
    private int maxChargeDuration;

    public DERAvailability(){
    }

    public DERAvailability(DERDto derDto){
        this.derDto = derDto;
    }

    public int getAvailabilityDuration() {
        return availabilityDuration;
    }

    public void setAvailabilityDuration(int availabilityDuration) {
        this.availabilityDuration = availabilityDuration;
    }

    public int getMaxChargeDuration() {
        return maxChargeDuration;
    }

    public void setMaxChargeDuration(int maxChargeDuration) {
        this.maxChargeDuration = maxChargeDuration;
    }
}
