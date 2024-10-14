
package interstore.Identity;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  SubscribableIdentifiedObjectRepository extends JpaRepository<SubscribableIdentifiedObjectEntity, Long> {
     List<SubscribableIdentifiedObjectEntity>findByFsaEntity_Id(Long fsaID);
     Optional <SubscribableIdentifiedObjectEntity>findFirstByIdAndFsaEntity_Id(Long id, Long fsaID); 

}
