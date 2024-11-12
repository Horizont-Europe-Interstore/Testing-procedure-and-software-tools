package interstore.DERCurve;
import interstore.Identity.IdentifiedObjectEntity;
import interstore.DERProgram.DERProgramEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "der_curve")
public class DERCurveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creationTime")
    public String creationTime;

    @Column(name = "curveType")
    public int curveType;

    @ManyToOne
    @JoinColumn(name = "der_program", nullable = false)
    private DERProgramEntity derProgram;

    @Column(name = "der_curve_link")
    private String derCurveLink;

//    @Column(name = "m_RID")
//    private String mRID;
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "version")
//    private String version;

    @Column(name = "xMultiplier")
    public int xMultiplier;

    @Column(name = "yMultiplier")
    public int yMultiplier;

    @Column(name = "yRefType")
    public int yRefType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "identified_object_entity")
    public IdentifiedObjectEntity identifiedObjectEntity;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curve_entity_id")
    public List<CurveData> curveData;

    public DERCurveEntity() {

    }

    public Long getId() {
        return id;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public int getCurveType() {
        return curveType;
    }

    public void setCurveType(int curveType) {
        this.curveType = curveType;
    }

    public DERProgramEntity getDerProgram() {
        return derProgram;
    }

    public void setDerProgram(DERProgramEntity derProgram) {
        this.derProgram = derProgram;
    }

    public String getDerCurveLink() {
        return derCurveLink;
    }

    public void setDerCurveLink(String derCurveLink) {
        this.derCurveLink = derCurveLink;
    }

    public int getxMultiplier() {
        return xMultiplier;
    }

    public void setxMultiplier(int xMultiplier) {
        this.xMultiplier = xMultiplier;
    }

    public int getyMultiplier() {
        return yMultiplier;
    }

    public void setyMultiplier(int yMultiplier) {
        this.yMultiplier = yMultiplier;
    }

    public int getyRefType() {
        return yRefType;
    }

    public void setyRefType(int yRefType) {
        this.yRefType = yRefType;
    }

//    public List<SubscribableIdentifiedObjectEntity> getSubscribableIdentifiedObject() {
//        return subscribabaleIdentifiedObjectList ;
//    }
//
//    public void setSubscribableIdentifiedObject(List<SubscribableIdentifiedObjectEntity> subscribableIdentifiedObject) {
//        this.subscribabaleIdentifiedObjectList = subscribableIdentifiedObject;
//    }


    public IdentifiedObjectEntity getIdentifiedObjectEntity() {
        return identifiedObjectEntity;
    }

    public void setIdentifiedObjectEntity(IdentifiedObjectEntity identifiedObjectEntity) {
        this.identifiedObjectEntity = identifiedObjectEntity;
    }

    public List<CurveData> getCurveData() {
        return curveData;
    }

    public void setCurveData(List<CurveData> curveData) {
        this.curveData = curveData;
    }
}
