package dac.reksio.secretary.s3.forward.dlt;

import dac.reksio.secretary.config.dlt.DltConfig;
import dac.reksio.secretary.config.dlt.DltConfigRepository;
import dac.reksio.secretary.config.dlt.DltProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
class DltClient {

    private static final String HASH_ENDPOINT = "/api/v1/hash/";
    private final DltConfigRepository dltConfigRepository;
    private final DltProperties dltProperties;
    private final RestTemplate restTemplate;

    DltHashResponse saveInDlt(String originalFilename, String hexHash) {
        DltConfig dltConfig = dltConfigRepository.getOne(DltConfigRepository.ID);
        DltFileDto dltFileDto = new DltFileDto(originalFilename, hexHash);

        String url = dltProperties.getUri(dltConfig.getDlt()) + HASH_ENDPOINT;
        return restTemplate.postForEntity(url, dltFileDto, DltHashResponse.class).getBody();
    }

    DltHashDto getHashOfFile(String filename) {
        DltConfig dltConfig = dltConfigRepository.getOne(DltConfigRepository.ID);
        String url = dltProperties.getUri(dltConfig.getDlt()) + HASH_ENDPOINT + filename;
        try {
            return restTemplate.getForObject(url, DltHashDto.class);
        } catch (RuntimeException ex) {
            log.warn("Could not read hash from notary", ex);
            return new DltHashDto("FAILED", "0x0");
        }
    }
}
