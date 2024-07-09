package interstore.DERCurve;

import interstore.Identity.IdentifiedObject;
import jakarta.persistence.*;

@Entity
public class DERCurveDto extends IdentifiedObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creationTime")
    private String creationTime;

    @Column(name = "curveType")
    private int curveType;

    @Column(name = "m_RID")
    private String mRID;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Column(name = "xMultiplier")
    private int xMultiplier;

    @Column(name = "yMultiplier")
    private int yMultiplier;

    @Column(name = "yRefType")
    private int yRefType;

    public DERCurveDto() {

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

    public String getmRID() {
        return mRID;
    }

    public void setmRID(String mRID) {
        this.mRID = mRID;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
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
}
