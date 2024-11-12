
package interstore.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  IdentifiedObjectRepository extends JpaRepository<IdentifiedObjectEntity, Long> {

}