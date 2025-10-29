package interstore.DERCurve;

import interstore.DERProgram.DERProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurveDataRepository extends JpaRepository<CurveData, Long> {
}
