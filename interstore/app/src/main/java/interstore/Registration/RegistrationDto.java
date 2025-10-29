package interstore.Registration;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import interstore.EndDevice.EndDeviceDto;
import jakarta.persistence.Entity; 
import com.fasterxml.jackson.annotation.JsonIgnore; 
@Entity
public class RegistrationDto   {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "pin")
    private Long pin; 
    @Column(name = "dateTimeRegistered")
    private String dateTimeRegistered;
    /* here the end device id is forigen key for particular object of 
     * end device there should be corresponding registrations it can be 
     * one to one or one to many , how can i impliment this 
     * Unidirectional relationship between RegistrationDto and EndDeviceDto 
     */
    @JsonIgnore
    @OneToOne
    @JoinColumn(name =  "endDeviceId", referencedColumnName = "id") 
    private EndDeviceDto endDevice;
    
    @Column(name = "link_rgid")
    private String linkRgid;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getPin() {
        return pin;
    }
    public void setPin( Long pin) {
        this.pin = pin;
    }
    public String getDateTimeRegistered() {
        return dateTimeRegistered;
    }
    public void setDateTimeRegistered(String dateTimeRegistered) {
        this.dateTimeRegistered = dateTimeRegistered;
    }

    public EndDeviceDto getEndDevice() {
        return endDevice;
    }
    public void setEndDevice(EndDeviceDto endDevice) {
        this.endDevice = endDevice;
    }
    public String getLinkRgid() {
        return linkRgid;
    }
    public void setLinkRgid(String linkRgid) {
        this.linkRgid = linkRgid;
    }
}




