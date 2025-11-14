package interstore.Time;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<TimeEntity, Long> {
    TimeEntity findByTimeLink(String timeLink);
}
