package dac.reksio.secretary.config.s3;

import org.springframework.stereotype.Component;

@Component
class S3ConfigDTOConverter {

    S3Config fromDTO(S3ConfigDTO s3ConfigDTO) {
        return S3Config.builder()
                       .id(S3ConfigRepository.ID)
                       .bucket(s3ConfigDTO.getBucket())
                       .key(s3ConfigDTO.getKey())
                       .secret(s3ConfigDTO.getSecret())
                       .region(s3ConfigDTO.getRegion())
                       .build();
    }

}
