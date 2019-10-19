package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.config.dlt.DltConfig;
import dac.reksio.secretary.config.dlt.DltConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class DltClient {

    private final DltConfigRepository dltConfigRepository;
    private final RestTemplate restTemplate;

    @Async
    public void saveInDlt(String originalFilename, String hexHash) {
        DltConfig dltConfig = dltConfigRepository.getOne(DltConfigRepository.ID);
        DltFileDto dltFileDto = new DltFileDto(originalFilename, hexHash);

        restTemplate.postForEntity(dltConfig.getUri() + "/api/v1/hash", dltFileDto, String.class);
    }
}
