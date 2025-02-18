package interstore.Identity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Respondable_subscribable_identified_object")
public class RespondableSubscribableIdentifiedObjectEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private Integer version = 0;

    @Column(name = "mRID", unique = true)
    private String mRID;

    @Column(name = "subscribable")
    private Integer subscribable = 0;


    public RespondableSubscribableIdentifiedObjectEntity() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getmRID() {
        return mRID;
    }

    public void setmRID(String mRID) {
        this.mRID = mRID;
    }

    public int getSubscribable() {
        return subscribable;
    }

    public void setSubscribable(int subscribable) {
        this.subscribable = subscribable;
    }
}
