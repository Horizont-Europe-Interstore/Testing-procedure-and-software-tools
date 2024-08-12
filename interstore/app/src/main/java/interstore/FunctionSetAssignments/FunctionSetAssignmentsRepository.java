package interstore.FunctionSetAssignments;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionSetAssignmentsRepository extends JpaRepository<FunctionSetAssignmentsEntity, Long> {
List<FunctionSetAssignmentsEntity> findByEndDeviceId(Long endDeviceId);
  Optional<FunctionSetAssignmentsEntity> findFirstByEndDeviceIdAndId(Long endDeviceId, Long functionSetAssignmentID); 

}
