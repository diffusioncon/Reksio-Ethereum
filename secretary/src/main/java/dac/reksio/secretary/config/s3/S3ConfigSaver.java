package dac.reksio.secretary.config.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class S3ConfigSaver {

    private final S3ConfigRepository s3ConfigRepository;
    private final S3ConfigDTOConverter s3ConfigDTOConverter;

    S3Config save(S3ConfigDTO s3ConfigDTO) {
        S3Config s3Config = s3ConfigDTOConverter.fromDTO(s3ConfigDTO);
        return s3ConfigRepository.save(s3Config);
    }

}
