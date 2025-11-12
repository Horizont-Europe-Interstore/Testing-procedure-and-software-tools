package interstore.DER.DERAvailabilty;

import interstore.DER.DerEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class DERAvailabilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "availabilityDuration")
    private Long availabilityDuration;  

    @Column(name = "maxChargeDuration")
    private Long maxChargeDuration;  

    @Column(name = "readingTimeAvailability")
    private String readingTimeAvailability; 

    @Column(name = "reserveChargePercent")
    private Double reserveChargePercent;  

    @Column(name = "reservePercent")
    private Double reservePercent;  

    @Column(name = "statVarAvail")
    private Double statVarAvail;  

    @Column(name = "statWAvail")
    private Double statWAvail;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "der_entity") 
    private DerEntity derEntity;

    @Column(name = "derAvailabilityLink")
    private String derAvailabilityLink;

    public Long getAvailabilityDuration() {
        return availabilityDuration;
    }

    public void setAvailabilityDuration(Long availabilityDuration) {
        this.availabilityDuration = availabilityDuration;
    }

    public Long getMaxChargeDuration() {
        return maxChargeDuration;
    }

    public void setMaxChargeDuration(Long maxChargeDuration) {
        this.maxChargeDuration = maxChargeDuration;
    }

    public Double getReserveChargePercent() {
        return reserveChargePercent;
    }

    public void setReserveChargePercent(Double reserveChargePercent) {
        this.reserveChargePercent = reserveChargePercent;
    }

    public Double getReservePercent() {
        return reservePercent;
    }

    public void setReservePercent(Double reservePercent) {
        this.reservePercent = reservePercent;
    }

    public Double getStatVarAvail() {
        return statVarAvail;
    }

    public void setStatVarAvail(Double statVarAvail) {
        this.statVarAvail = statVarAvail;
    }
   
    public Double getStatWAvail() {
        return statWAvail;
    }

    public void setStatWAvail(Double statWAvail) {
        this.statWAvail = statWAvail;
    }

    public String getReadingTimeAvailability() {
        return readingTimeAvailability;
    }

    public void setReadingTimeAvailability(String readingTimeAvailability) {
        this.readingTimeAvailability = readingTimeAvailability;
    }

    public Long getId() {
        return id;
    }

    public DerEntity getDerEntity() {
        return derEntity;
    }

    public void setDerEntity(DerEntity derEntity) {
        this.derEntity = derEntity;
    }
    
    public String getDerAvailabilityLink() {
        return derAvailabilityLink;
    }

    public void setDerAvailabilityLink(String derAvailabilityLink) {
        this.derAvailabilityLink = derAvailabilityLink;
    }

}
