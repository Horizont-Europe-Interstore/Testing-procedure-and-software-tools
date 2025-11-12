package interstore.DER.DERStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DERStatusRepository extends JpaRepository<DERStatusEntity, Long>{
    
}
