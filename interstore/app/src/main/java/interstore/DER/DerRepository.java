package interstore.DER;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import interstore.DERProgram.DERProgramEntity;


public interface DerRepository extends JpaRepository<DerEntity, Long> {
    List<DERProgramEntity>findByEndDeviceId(Long endDeviceId);
    Optional <DerEntity>findFirstByIdAndEndDeviceId(Long id, Long endDeviceId); 
}
