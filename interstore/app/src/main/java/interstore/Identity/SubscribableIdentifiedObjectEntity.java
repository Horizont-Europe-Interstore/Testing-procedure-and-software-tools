package interstore.Identity;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "subscribable_identified_object")
public class SubscribableIdentifiedObjectEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

  
    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private Integer version;

    @Column(name = "mRID", unique = true)
    private String mRID;

    public SubscribableIdentifiedObjectEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

   
}



































/* do the validation of the attributes such mRID, version, description
 * this class has to be an entity becasue it has to save this in a db
 * 
public class SubscribableIdentifiedObject  {
    private SubscribableResource subscribableResource; // Composition
    private String description ;
    private Integer version ;
    private String mRID ;

   
    public SubscribableIdentifiedObject() {
    
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
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
    




    public SubscribableResource getSubscribableResource() {
        return subscribableResource;
    }

    public void setSubscribableResource(SubscribableResource subscribableResource) {
        this.subscribableResource = subscribableResource;
    }

   
}

*/

