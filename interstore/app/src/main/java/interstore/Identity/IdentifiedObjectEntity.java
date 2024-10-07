package interstore.Identity;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "identified_object")
public class IdentifiedObjectEntity implements Serializable {

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

    public IdentifiedObjectEntity() {
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






//package interstore.Identity;
//
//// the version here comes to play , this vesion
//// can be a type . now treating it like a string
//public class IdentifiedObjectEntity {
//    private byte mRID;
//    private String version;
//    private String description;
//
//   public IdentifiedObjectEntity(byte mRID, String version, String description){
//       this.mRID = mRID;
//       this.version = version;
//       this.description = description;
//   }
//
//    public IdentifiedObjectEntity(){
//
//    }
//
//   public byte getMRID(){
//       return mRID;
//   }
//   public String getVersion(){
//       return version;
//   }
//   public String getDescription(){
//     return description;
//   }
//  // setter for the above
//    public void setMRID(byte mRID){
//        this.mRID = mRID;
//    }
//    public void setVersion(String version){
//        this.version = version;
//    }
//    public void setDescription(String description){
//        this.description = description;
//    }
//
//
//
//
//}
