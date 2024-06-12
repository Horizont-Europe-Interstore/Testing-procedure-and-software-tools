package interstore.Time;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeDtoRepository extends JpaRepository<TimeDto, Long> {
    TimeDto findByTimeLink(String timeLink);
}
