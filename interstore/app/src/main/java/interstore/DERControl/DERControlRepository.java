package interstore.DERControl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DERControlRepository extends JpaRepository<DERControlEntity, Long> {
    List<DERControlEntity> findByDerProgramId(Long derProgramId);
    Optional<DERControlEntity> findFirstByDerProgramIdAndId(Long derProgramId, Long derControlId);
}
