package interstore.Registration;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface RegistrationRepository extends JpaRepository<RegistrationDto, Long> {
    List<RegistrationDto> findByEndDeviceId(Long endDeviceId);
   // RegistrationDto findTopByEndDeviceId(Long endDeviceId);
    //RegistrationDto findTopByEndDeviceId(Long endDeviceId); 
   // RegistrationDto findById(Long registrationID);
    //Optional<RegistrationDto> findFirstByEndDeviceId(Long registrationID);
    //RegistrationDto findByEndDeviceIDAndRegistrationId(Long endDeviceId, Long registrationID);
    Optional<RegistrationDto> findFirstByEndDeviceIdAndId(Long endDeviceId, Long registrationID); 

}


