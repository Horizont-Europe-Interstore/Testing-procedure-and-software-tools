package interstore.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribableResourceRepository extends JpaRepository<SubscribableResourceEntity, Long> {

    
}
