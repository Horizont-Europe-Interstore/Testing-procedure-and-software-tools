package interstore.DERControl;

import interstore.DERProgram.DERProgramEntity;
import interstore.Events.RandomizableEventEntity;
import interstore.Identity.SubscribableResourceEntity;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "der_control")
public class DERControlEntity extends RandomizableEventEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "deviceCategory")
    public Integer deviceCategory;

    @ManyToOne
    @JoinColumn(name = "der_program", nullable = false)
    private DERProgramEntity derProgram;

    @Column(name = "der_control_link")
    private String derControlLink;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_control_base")
    private DERControlBase der_control_base;

    public DERControlEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public Integer getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(Integer deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public DERControlBase getDer_control_base() {
        return der_control_base;
    }

    public void setDer_control_base(DERControlBase der_control_base) {
        this.der_control_base = der_control_base;
    }

    public DERProgramEntity getDerProgram() {
        return derProgram;
    }

    public void setDerProgram(DERProgramEntity derProgram) {
        this.derProgram = derProgram;
    }

    public String getDerControlLink() {
        return derControlLink;
    }

    public void setDerControlLink(String derControlLink) {
        this.derControlLink = derControlLink;
    }
}
