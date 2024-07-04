package interstore.FunctionSetAssignments;

import interstore.Identity.SubscribableList;
import interstore.Types.SubscribableType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FunctionSetAssignmentsList extends SubscribableList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fsa_list_link")
    private String fsaListLink;
    @OneToMany(
            mappedBy = "fsaList",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FunctionSetAssignments> fsa = new ArrayList<>();

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "endDev_id")
//    private EndDeviceDto endDeviceDto;

//    UInt32 pollRate = new UInt32(900);
//    EndDeviceDto endDeviceDto;
//    static EndDeviceImpl endDeviceImpl = new EndDeviceImpl();
//    Map<String, Object> endDeviceList = new HashMap<>();

//    Map<String, Object> fsaList = new HashMap<>();
//    Link link;


    public FunctionSetAssignmentsList(String href, SubscribableType subscribable) {
        super(href, subscribable);
    }

    public FunctionSetAssignmentsList() {

    }

    public Long getId() {
        return id;
    }

    public FunctionSetAssignmentsList(String fsaListLink) {
        this.fsaListLink = fsaListLink;
    }

    public String getFsaListLink() {
        return fsaListLink;
    }

    public void setFsaListLink(String fsaListLink) {
        this.fsaListLink = fsaListLink;
    }

    public List<FunctionSetAssignments> getFsa() {
        return fsa;
    }

    public void setFsa(List<FunctionSetAssignments> fsa) {
        this.fsa = fsa;
    }

    public void addFsa(FunctionSetAssignments fsa) {
        this.fsa.add(fsa);
    }

    //    public void generateFSAList(){
//
//        this.endDeviceList = endDeviceImpl.getEndDeviceListLinks();
//
//        for (int i = 0; i < this.endDeviceList.size(); i++){
//            List<String> elements=new ArrayList<String>();
//            Map<String, Object> endDevice = this.endDeviceImpl.getEndDeviceById(Integer.toString(i+1));
//            Object endDevLink = endDevice.get("functionSetAssignmentsListLink");
//            for (int j = 0; j < 3; j++ ){
//                int id = j + 1;
//                String generatedLink = endDevLink + "/" + id;
//                elements.add(generatedLink);
//            }
//            this.fsaList.put(Integer.toString(i+1),elements);
//        }
//    }


//    public Object getFSAList(String id){
//        this.generateFSAList();
//        return this.fsaList.get(id);
//    }


}

