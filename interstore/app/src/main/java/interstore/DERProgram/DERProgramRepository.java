package interstore.DERProgram;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface DERProgramRepository extends JpaRepository<DERProgramEntity, Long> {
   
   List<DERProgramEntity>findByFsaEntity_Id(Long fsaID);
  
}
 
