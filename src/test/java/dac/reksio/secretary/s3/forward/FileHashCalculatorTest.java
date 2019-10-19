package dac.reksio.secretary.s3.forward;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

class FileHashCalculatorTest {

    private final FileHashCalculator testObj = new FileHashCalculator();

    @Test
    void shouldReturnSha256OfFile() {
        // given
        MockMultipartFile file = new MockMultipartFile("name", "content".getBytes());
        String expectedFileHash = "ed7002b439e9ac845f22357d822bac1444730fbdb6016d3ec9432297b9ec9f73";

        // when
        String hexHash = testObj.calculateHash(file);

        // then
        assertThat(hexHash).isEqualTo(expectedFileHash);
    }
}