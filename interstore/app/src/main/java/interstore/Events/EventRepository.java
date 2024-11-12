package interstore.Events;

import interstore.DERProgram.DERProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
