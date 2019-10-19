package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.s3.S3UploadRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class FileHashCalculatorTest {

    private final FileHashCalculator testObj = new FileHashCalculator();

    @Test
    void shouldReturnSha256OfFile() {
        // given
        S3UploadRequest file = S3UploadRequest.builder()
                                              .fileContent("content".getBytes(StandardCharsets.UTF_8))
                                              .build();
        String expectedFileHash = "0xed7002b439e9ac845f22357d822bac1444730fbdb6016d3ec9432297b9ec9f73";

        // when
        String hexHash = testObj.calculateHash(file);

        // then
        assertThat(hexHash).isEqualTo(expectedFileHash);
    }
}