package interstore.FunctionSetAssignments;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import interstore.EndDevice.EndDeviceDto;

public interface FunctionSetAssignmentsRepository extends JpaRepository<FunctionSetAssignmentsEntity, Long> {
List<FunctionSetAssignmentsEntity> findByEndDeviceId(Long endDeviceId);
List<FunctionSetAssignmentsEntity> findByEndDevice(EndDeviceDto endDevice);
Optional <FunctionSetAssignmentsEntity> findFirstByEndDeviceIdAndId(Long endDeviceId, Long functionSetAssignmentID); 
FunctionSetAssignmentsEntity findFsaById(Long functionSetAssignmentID);

@Query("SELECT MAX(fsa.id) FROM FunctionSetAssignmentsEntity fsa WHERE fsa.endDevice.id = :endDeviceId")
Long findCurrentFsaIdByEndDeviceId(@Param("endDeviceId") Long endDeviceId);





}

