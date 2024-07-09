package interstore.DER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DERListImpl {

    @Autowired
    DERListRepository derListRepository;
    public String getDERList(String DERListLink){
        DERList derList = derListRepository.findByDerListLink(DERListLink);
        if (derList != null) {
            return derList.getId().toString();
        }
        return null;
    }
}
