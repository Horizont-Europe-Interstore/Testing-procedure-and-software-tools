package interstore.DeviceCapability;
import java.util.HashSet;
import java.util.logging.Logger;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import java.util.Set;
@Entity
public class DeviceCapabilityDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
  
   
    }

   
 

   
   
   
    




