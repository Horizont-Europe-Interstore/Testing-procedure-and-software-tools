package interstore.Registration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    List<RegistrationEntity> findByEndDeviceID(Long endDeviceID);
    Optional<RegistrationEntity> findFirstByEndDeviceIDAndId(Long endDeviceID, Long registrationID);
}
