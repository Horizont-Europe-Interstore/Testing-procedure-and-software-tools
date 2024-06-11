package interstore.DER;

import interstore.Identity.SubscribableResource;
import jakarta.persistence.*;

import java.util.logging.Logger;
@Entity
public class DERDto extends SubscribableResource {
    private static final Logger LOGGER = Logger.getLogger(DERDto.class.getName());
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "der_link")
    private String derLink;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "der_capability_id")
    private DERCapability derCapability;

    @Column(name = "der_capability_link")
    private String derCapabilityLink;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "der_settings_id")
    private DERSettings derSettings;

    @Column(name = "der_settings_link")
    private String derSettingsLink;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "der_status_id")
    private DERStatus derStatus;

    @Column(name = "der_status_link")
    private String derStatusLink;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "der_availability_id")
    private DERAvailability derAvailability;

    @Column(name = "der_availability_link")
    private String derAvailabilityLink;

    @ManyToOne
    @JoinColumn(name = "der_list_id")
    private DERList derList;



    public DERDto(DERList derList){
        this.derList = derList;
    }
    public DERDto(){

    }

    // Setters
    public void setDerLink(String derLink) {
        this.derLink = derLink;
    }

    public void setDerList(DERList derList) {
        this.derList = derList;
    }

    public void setDerCapabilityLink(String derCapabilityLink) {
        this.derCapabilityLink = derCapabilityLink;
    }

    public void setDerCapability(DERCapability derCapability) {
        this.derCapability = derCapability;
    }

    public void setDerSettingsLink(String derSettingsLink) {
        this.derSettingsLink = derSettingsLink;
    }

    public void setDerStatusLink(String derStatusLink) {
        this.derStatusLink = derStatusLink;
    }

    public void setDerAvailabilityLink(String derAvailabilityLink) {
        this.derAvailabilityLink = derAvailabilityLink;
    }

    public void setDerAvailability(DERAvailability derAvailability) {
        this.derAvailability = derAvailability;
    }

    public void setDerSettings(DERSettings derSettings) {
        this.derSettings = derSettings;
    }

    public void setDerStatus(DERStatus derStatus) {
        this.derStatus = derStatus;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getDerLink() {
        return derLink;
    }

    public String getDerCapabilityLink() {
        return derCapabilityLink;
    }

    public String getDerSettingsLink() {
        return derSettingsLink;
    }

    public String getDerStatusLink() {
        return derStatusLink;
    }

    public String getDerAvailabilityLink() {
        return derAvailabilityLink;
    }

    public DERAvailability getDerAvailability() {
        return derAvailability;
    }

    public DERCapability getDerCapability() {
        return derCapability;
    }

    public DERSettings getDerSettings() {
        return derSettings;
    }

    public DERStatus getDerStatus() {
        return derStatus;
    }
}
