package interstore.EndDevice;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
public interface EndDeviceRepository extends JpaRepository<EndDeviceEntity, Long> {
    Optional<EndDeviceEntity> findBysFDI(Long id);
}
