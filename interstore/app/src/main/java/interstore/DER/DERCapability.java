package interstore.DER;

import interstore.Identity.Resource;
import jakarta.persistence.*;

import java.util.logging.Logger;

@Entity
public class DERCapability extends Resource {
    private static final Logger LOGGER = Logger.getLogger(DERCapability.class.getName());
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "derCapability")
    private DERDto derDto;

    @Column(name = "rtgAbnormalCategory")
    private int rtgAbnormalCategory;

    @Column(name = "rtgNormalCategory")
    private int rtgNormalCategory;

    @Column(name = "modesSupported")
    private int modesSupported;

    public DERCapability(DERDto derDto){
        this.derDto = derDto;
    }

    public DERCapability(){

    }

    public int getRtgAbnormalCategory() {
        return rtgAbnormalCategory;
    }

    public void setRtgAbnormalCategory(int rtgAbnormalCategory) {
        this.rtgAbnormalCategory = rtgAbnormalCategory;
    }

    public int getRtgNormalCategory() {
        return rtgNormalCategory;
    }

    public void setRtgNormalCategory(int rtgNormalCategory) {
        this.rtgNormalCategory = rtgNormalCategory;
    }

    public int getModesSupported() {
        return modesSupported;
    }

    public void setModesSupported(int modesSupported) {
        this.modesSupported = modesSupported;
    }

    public DERDto getDerDto() {
        return derDto;
    }

    public void setDerDto(DERDto derDto) {
        this.derDto = derDto;
    }
}
