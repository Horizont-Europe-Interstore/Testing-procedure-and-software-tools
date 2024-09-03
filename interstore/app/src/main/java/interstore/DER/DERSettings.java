package interstore.DER;

//import interstore.Identity.SubscribableResource    extends SubscribableResource;
import jakarta.persistence.*;

@Entity
public class DERSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "derSettings")
    private DERDto derDto;

    @Column(name = "modesEnabled")
    private int modesEnabled;

    @Column(name = "setESDelay")
    private int setESDelay;

    @Column(name = "setGradW")
    private int setGradW;

    public DERSettings(){

    }

    public DERSettings(DERDto derDto){
        this.derDto = derDto;
    }

    public DERDto getDerDto() {
        return derDto;
    }

    public void setDerDto(DERDto derDto) {
        this.derDto = derDto;
    }

    public int getModesEnabled() {
        return modesEnabled;
    }

    public void setModesEnabled(int modesEnabled) {
        this.modesEnabled = modesEnabled;
    }

    public int getSetESDelay() {
        return setESDelay;
    }

    public void setSetESDelay(int setESDelay) {
        this.setESDelay = setESDelay;
    }

    public int getSetGradW() {
        return setGradW;
    }

    public void setSetGradW(int setGradW) {
        this.setGradW = setGradW;
    }
}
