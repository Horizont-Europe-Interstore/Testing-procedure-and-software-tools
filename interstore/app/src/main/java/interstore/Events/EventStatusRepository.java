package interstore.Events;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventStatusRepository extends JpaRepository<EventStatusEntity, Long> {
}
