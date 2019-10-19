package dac.reksio.secretary.config.dlt;

import org.springframework.stereotype.Component;

@Component
class DltConfigDTOConverter {

    DltConfig fromDTO(DltConfigDTO dltConfigDTO) {
        return DltConfig.builder()
                        .dlt(dltConfigDTO.getDlt())
                        .id(DltConfigRepository.ID)
                        .build();
    }

}
