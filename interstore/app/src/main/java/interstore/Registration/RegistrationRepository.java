package interstore.Registration;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    List<RegistrationEntity> findByEndDevice_Id(Long endDeviceId);
    Optional<RegistrationEntity> findFirstByEndDevice_IdAndId(Long endDeviceId, Long registrationID);
    
    @Query("SELECT r FROM RegistrationEntity r WHERE r.endDevice.id = :endDeviceId")
    List<RegistrationEntity> findByEndDeviceIdNative(@Param("endDeviceId") Long endDeviceId); 

}


