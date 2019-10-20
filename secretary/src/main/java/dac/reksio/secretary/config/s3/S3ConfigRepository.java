package dac.reksio.secretary.config.s3;

import org.springframework.data.jpa.repository.JpaRepository;

public interface S3ConfigRepository extends JpaRepository<S3Config, String> {
    String ID = "s3";
}
