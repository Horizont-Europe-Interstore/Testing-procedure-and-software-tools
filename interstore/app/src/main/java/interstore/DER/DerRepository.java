package interstore.DER;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DerRepository extends JpaRepository<DerEntity, Long> {
    List<DerEntity>findByEndDeviceId(Long endDeviceId);
    Optional <DerEntity>findFirstByEndDeviceIdAndId(Long endDeviceId, Long id);
}
