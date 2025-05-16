package interstore.DeviceCapability;

import interstore.FunctionSetAssignments.FunctionSetAssignmentBase;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Entity
public class DeviceCapabilityDto  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "href")
    private String href = "/dcap";

    @Column(name = "EndDeviceListLink")
    private String endDeviceListLink;

    @Column(name = "TimeLink")
    private String timeLink;

    @Column(name = "MirrorUsagePointListLink")
    private String mirrorUsagePointListLink;

    @Column(name = "SelfDeviceLink")
    private String selfDeviceLink;

    @ElementCollection
    @CollectionTable(name = "device_capability_links")
    @Column(name = "link")
    private Set<String> links = new HashSet<>();

    private static final Logger LOGGER = Logger.getLogger(DeviceCapabilityDto.class.getName()); 
     
    
    public DeviceCapabilityDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Set<String> getLinks() {
        return links;
    }

    public void setLinks(Set<String> links) {
        this.links = links;
    }

    public void addLink(String url) {
        this.links.add(url);
    }

    public String getEndDeviceListLink() {
        return endDeviceListLink;
    }

    public void setEndDeviceListLink(String endDeviceListLink) {
        this.endDeviceListLink = endDeviceListLink;
    }

    public String getTimeLink() {
        return timeLink;
    }

    public void setTimeLink(String timeLink) {
        this.timeLink = timeLink;
    }

    public String getMirrorUsagePointListLink() {
        return mirrorUsagePointListLink;
    }

    public void setMirrorUsagePointListLink(String mirrorUsagePointListLink) {
        this.mirrorUsagePointListLink = mirrorUsagePointListLink;
    }

    public String getSelfDeviceLink() {
        return selfDeviceLink;
    }

    public void setSelfDeviceLink(String selfDeviceLink) {
        this.selfDeviceLink = selfDeviceLink;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

   
 

   
   
   
    




