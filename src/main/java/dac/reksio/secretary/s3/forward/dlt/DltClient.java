package dac.reksio.secretary.s3.forward.dlt;

import dac.reksio.secretary.config.dlt.DltConfig;
import dac.reksio.secretary.config.dlt.DltConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DltClient {

    public static final String HASH_ENDPOINT = "/api/v1/hash/";
    private final DltConfigRepository dltConfigRepository;
    private final RestTemplate restTemplate;

    @Async
    public void saveInDlt(String originalFilename, String hexHash) {
        DltConfig dltConfig = dltConfigRepository.getOne(DltConfigRepository.ID);
        DltFileDto dltFileDto = new DltFileDto(originalFilename, hexHash);

        restTemplate.postForEntity(dltConfig.getUri() + HASH_ENDPOINT, dltFileDto, String.class);
    }

    public DltHashDto getHashOfFile(String filename) {
        DltConfig dltConfig = dltConfigRepository.getOne(DltConfigRepository.ID);
        try {
            return restTemplate.getForObject(dltConfig.getUri() + HASH_ENDPOINT + filename, DltHashDto.class);
        } catch (RuntimeException ex) {
            log.warn("Could not read hash from notary", ex);
            return new DltHashDto("FAILED", "0x0");
        }
    }
}
