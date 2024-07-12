package interstore.DER;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DERList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "der_list_link")
    private String derListLink;
    @OneToMany(
            mappedBy = "derList",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DERDto> derDto = new ArrayList<>();


    public DERList(String derListLink){
        this.derListLink = derListLink;

    }

    public DERList(){

    }

    public Long getId() {
        return id;
    }

    public String getDerListLink() {
        return derListLink;
    }

    public void setDerListLink(String derListLink) {
        this.derListLink = derListLink;
    }

    public List<DERDto> getDerDto() {
        return derDto;
    }

    public void setDerDto(List<DERDto> derDto) {
        this.derDto = derDto;
    }

    public void addDerDto(DERDto derDto){
        this.derDto.add(derDto);
    }

    public void printDERDtoList() {
        for (DERDto derDto : this.derDto) {
            System.out.println("DERDto ID: " + derDto.getId());
        }
    }
}
