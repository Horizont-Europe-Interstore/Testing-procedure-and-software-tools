package interstore.Registration;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;

@Entity
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pin")
    private Long pin;

    @Column(name = "dateTimeRegistered")
    private String dateTimeRegistered;

    @Column(name = "endDeviceID")
    private Long endDeviceID ;

    @Column(name = "link_rgid")
    private String linkRgid;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPin() { return pin; }
    public void setPin(Long pin) { this.pin = pin; }

    public String getDateTimeRegistered() { return dateTimeRegistered; }
    public void setDateTimeRegistered(String dateTimeRegistered) { this.dateTimeRegistered = dateTimeRegistered; }

    public Long getEndDeviceId() { return endDeviceID; }
    public void setEndDeviceId(Long endDeviceID) { this.endDeviceID = endDeviceID; }

    public String getLinkRgid() { return linkRgid; }
    public void setLinkRgid(String linkRgid) { this.linkRgid = linkRgid; }
}
