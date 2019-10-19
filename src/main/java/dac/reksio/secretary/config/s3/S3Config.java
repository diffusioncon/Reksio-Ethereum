package dac.reksio.secretary.config.s3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.regions.Region;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3Config {
    @Id
    private String id;
    private String apiUrl;
    private String key;
    private String secret;
    private String region;
    private String bucket;
}
