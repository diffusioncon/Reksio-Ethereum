package dac.reksio.secretary.config.dlt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DltConfigSaver {

    private final DltConfigDTOConverter dltConfigDTOConverter;
    private final DltConfigRepository dltConfigRepository;

    DltConfig save(DltConfigDTO dltConfigDTO) {
        DltConfig dltConfig = dltConfigDTOConverter.fromDTO(dltConfigDTO);
        return dltConfigRepository.save(dltConfig);
    }

}
