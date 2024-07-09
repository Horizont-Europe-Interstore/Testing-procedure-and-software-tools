package interstore.DER;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DERListRepository extends JpaRepository<DERList, Long> {
    DERList findByDerListLink(String link);
}
