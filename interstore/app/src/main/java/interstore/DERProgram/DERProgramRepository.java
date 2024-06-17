package interstore.DERProgram;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DERProgramRepository extends JpaRepository<DERProgram, Long> {
    List<DERProgram> findAllByDerpListLink(String derpListLink);
}
