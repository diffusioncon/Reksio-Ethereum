package dac.reksio.secretary.config.s3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class S3ConfigDTO {
    private String apiUrl;
    private String bucket;
    private String key;
    private String secret;
    private String region;
}
