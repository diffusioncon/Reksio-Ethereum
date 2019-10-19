package dac.reksio.secretary.config;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
class S3Config {
    @Id
    private String id = "s3";
    private String apiUrl;
}
