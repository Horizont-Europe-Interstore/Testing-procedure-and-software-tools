package interstore.DER;

import interstore.Identity.Link;
import jakarta.persistence.*;

@Entity
public class DERStatus extends Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "derStatus")
    private DERDto derDto;

    @Column(name = "alarmStatus")
    private int alarmStatus;

    @Column(name = "genConnectStatus")
    private int genConnectStatus;

    @Column(name = "inverterStatus")
    private int inverterStatus;

    public DERStatus(DERDto derDto) {
        this.derDto = derDto;
    }

    public DERStatus() {
    }

    public DERDto getDerDto() {
        return derDto;
    }

    public void setDerDto(DERDto derDto) {
        this.derDto = derDto;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public int getGenConnectStatus() {
        return genConnectStatus;
    }

    public void setGenConnectStatus(int genConnectStatus) {
        this.genConnectStatus = genConnectStatus;
    }

    public int getInverterStatus() {
        return inverterStatus;
    }

    public void setInverterStatus(int inverterStatus) {
        this.inverterStatus = inverterStatus;
    }
}
