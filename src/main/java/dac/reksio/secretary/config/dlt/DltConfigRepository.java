package dac.reksio.secretary.config.dlt;

import org.springframework.data.jpa.repository.JpaRepository;

interface DltConfigRepository extends JpaRepository<DltConfig, String> {
    String ID = "dlt";
}
