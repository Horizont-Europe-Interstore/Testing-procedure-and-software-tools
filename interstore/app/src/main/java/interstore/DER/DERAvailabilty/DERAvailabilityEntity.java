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

    @Column(name = "readingTime")
    private Long readingTime; 

    @Column(name = "reserveChargePercent")
    private Integer reserveChargePercent;  

    @Column(name = "reservePercent")
    private Integer reservePercent;  

    @Column(name = "statVarAvailMultiplier")
    private Integer statVarAvailMultiplier;  
    @Column(name = "statVarAvailValue")
    private Integer statVarAvailValue;

    @Column(name = "statWAvailMultiplier")
    private Integer statWAvailMultiplier;
    @Column(name = "statWAvailValue")
    private Integer statWAvailValue;

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

    public Integer getReserveChargePercent() {
        return reserveChargePercent;
    }

    public void setReserveChargePercent(Integer reserveChargePercent) {
        this.reserveChargePercent = reserveChargePercent;
    }

    public Integer getReservePercent() {
        return reservePercent;
    }

    public void setReservePercent(Integer reservePercent) {
        this.reservePercent = reservePercent;
    }

    public Integer getStatVarAvailMultiplier() {
        return statVarAvailMultiplier;
    }

    public void setStatVarAvailMultiplier(Integer statVarAvailMultiplier) {
        this.statVarAvailMultiplier = statVarAvailMultiplier;
    }

    public Integer getStatVarAvailValue() {
        return statVarAvailValue;
    }

    public void setStatVarAvailValue(Integer statVarAvailValue) {
        this.statVarAvailValue = statVarAvailValue;
    }
   
    public Integer getStatWAvailMultiplier() {
        return statWAvailMultiplier;
    }

    public void setStatWAvailMultiplier(Integer statWAvailMultiplier) {
        this.statWAvailMultiplier = statWAvailMultiplier;
    }

    public Integer getStatWAvailValue() {
        return statWAvailValue;
    }

    public void setStatWAvailValue(Integer statWAvailValue) {
        this.statWAvailValue = statWAvailValue;
    }

    public Long getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(Long readingTime) {
        this.readingTime = readingTime;
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
