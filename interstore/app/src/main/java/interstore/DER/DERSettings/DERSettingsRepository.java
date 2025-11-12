package interstore.DER.DERSettings;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DERSettingsRepository extends JpaRepository<DERSettingsEntity, Long>{
    
}
