package interstore.EndDevice.DeviceInformation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import interstore.EndDevice.EndDeviceEntity;



public interface DeviceInformationRepository extends JpaRepository<DeviceInformationEntity, Long> {
    Optional<DeviceInformationEntity> findByEndDeviceEntity(EndDeviceEntity endDeviceEntity);
}