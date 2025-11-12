package interstore.DER.DERCapability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DERCapabilityRepository extends JpaRepository<DERCapabilityEntity, Long>{
    
}
