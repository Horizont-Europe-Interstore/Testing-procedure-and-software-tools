package interstore.DERProgram;
import jakarta.persistence.*;
import java.io.Serializable;
import interstore.FunctionSetAssignments.FunctionSetAssignmentsEntity;
import interstore.Identity.*; 

@Entity
@Table(name = "der_program")
public class DERProgramEntity  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "primacy")
    private Short primacy;

   
    @Column(name = "default_DER_Control_Link")
    private String defaultDERControlLink;

    @Column(name = "active_DER_Control_List_Link")
    private String activeDERControlListLink;

    @Column(name = "DER_Control_List_Link")
    private String derControlListLink;

    @Column(name = "DER_Curve_List_Link")
    private String derCurveListLink;
     
    @ManyToOne 
    @JoinColumn(name = "fsa_id", nullable = false)  
    private FunctionSetAssignmentsEntity fsaEntity;
  
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "subscribabale_identified_Object")
    private SubscribableIdentifiedObjectEntity subscribabaleIdentifiedObjectList;
   
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "subscribabale_Resource") 
    private SubscribableResourceEntity subscribableResourceList;


    public DERProgramEntity(){

    }

    public Long getId() {
        return id;
    }

    public Short getPrimacy() {
        return primacy;
    }

    public void setPrimacy(Short primacy) {
        this.primacy = primacy;
    }
    

    public FunctionSetAssignmentsEntity getFunctionSetAssignmentsEntity() {
        return fsaEntity;
    }

    public void setFunctionSetAssignmentEntity(FunctionSetAssignmentsEntity fsaEntity) {
        this.fsaEntity = fsaEntity;
    } 
   
    public SubscribableIdentifiedObjectEntity getSubscribableIdentifiedObject() {
        return subscribabaleIdentifiedObjectList ;
    }

    public void setSubscribableIdentifiedObject(SubscribableIdentifiedObjectEntity subscribableIdentifiedObject) {
        this.subscribabaleIdentifiedObjectList = subscribableIdentifiedObject;
    }
    public SubscribableResourceEntity getSubscribableResource() {
        return subscribableResourceList;
    }
    public void setSubscribableResource(SubscribableResourceEntity subscribableResource) {
        this.subscribableResourceList = subscribableResource;
    }

    public String getDefaultDERControlLink() {
        return defaultDERControlLink;
    }

    public void setDefaultDERControlLink(String defaultDERControlLink) {
        this.defaultDERControlLink = defaultDERControlLink;
        
    }

    public String getActiveDERControlListLink() {
        return activeDERControlListLink;
    }

    public void setActiveDERControlListLink(String activeDERControlListLink) {
        this.activeDERControlListLink = activeDERControlListLink;  
        
    }

    public String getDERControlListLink() {
        return derControlListLink;
    }

    public void setDERControlListLink(String derControlListLink) {
        this.derControlListLink = derControlListLink;
        
    }

    public String getDERCurveListLink() {
        return derCurveListLink;
    }

    public void setDERCurveListLink(String derCurveListLink) {
        this.derCurveListLink = derCurveListLink;
        
    }


 

    
    }

   