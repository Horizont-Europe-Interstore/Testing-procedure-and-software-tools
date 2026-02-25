package interstore.Registration;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    List<RegistrationEntity> findByEndDeviceId(Long endDeviceId);
    Optional<RegistrationEntity> findFirstByEndDeviceIdAndId(Long endDeviceId, Long registrationID); 

}


