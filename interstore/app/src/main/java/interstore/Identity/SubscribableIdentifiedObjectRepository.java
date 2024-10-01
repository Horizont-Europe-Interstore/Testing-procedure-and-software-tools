
package interstore.Identity;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  SubscribableIdentifiedObjectRepository extends JpaRepository<SubscribableIdentifiedObjectEntity, Long> {
     List<SubscribableIdentifiedObjectEntity>findByFsaEntity_Id(Long fsaID);

}
