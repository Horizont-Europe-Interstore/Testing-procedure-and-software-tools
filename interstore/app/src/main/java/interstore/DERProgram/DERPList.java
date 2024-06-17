package interstore.DERProgram;

import interstore.Identity.SubscribableList;
import interstore.Types.SubscribableType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DERPList extends SubscribableList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "derp_list_link")
    private String derpListLink;

    @OneToMany(
            mappedBy = "derpList",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DERProgram> derpDto = new ArrayList<>();
//    public UInt32 pollRate = new UInt32(900);

    public DERPList(String href, SubscribableType subscribable){
        super(href,subscribable);
    }

    public DERPList(){

    }

    public DERPList(String derpListLink){
        this.derpListLink = derpListLink;
    }

    public Long getId() {
        return id;
    }

    public String getDerpListLink() {
        return derpListLink;
    }

    public void setDerpListLink(String derpListLink) {
        this.derpListLink = derpListLink;
    }

    public List<DERProgram> getDerpDto() {
        return derpDto;
    }

    public void setDerpDto(List<DERProgram> derpDtos) {
        this.derpDto = derpDtos;
    }

    public void addDerpDto(DERProgram derpDto) {
        this.derpDto.add(derpDto);
    }
}
