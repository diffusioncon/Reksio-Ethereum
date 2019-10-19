package dac.reksio.secretary.config.dlt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DltConfigRepository extends JpaRepository<DltConfig, String> {
    String ID = "dlt";
}
