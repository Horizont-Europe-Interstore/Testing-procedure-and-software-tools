package interstore.DERProgram;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface DERProgramRepository extends JpaRepository<DERProgramEntity, Long> {
   
   List<DERProgramEntity>findByFsaEntity_Id(Long fsaID);
   Optional <DERProgramEntity> findFirstByIdAndFsaEntity_Id(Long id, Long fsaID); 
  
}
 
