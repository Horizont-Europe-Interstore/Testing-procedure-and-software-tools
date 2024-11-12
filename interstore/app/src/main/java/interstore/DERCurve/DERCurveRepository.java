package interstore.DERCurve;

import interstore.DERProgram.DERProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DERCurveRepository extends JpaRepository<DERCurveEntity, Long> {
    List<DERCurveEntity> findByDerProgramId(Long derProgramId);
    Optional<DERCurveEntity> findFirstByDerProgramIdAndId(Long derProgramId, Long derCurveId);
}
