package dac.reksio.secretary.s3.forward;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class FileHashCalculatorTest {

    private final FileHashCalculator testObj = new FileHashCalculator();

    @Test
    void shouldReturnSha256OfFile() {
        // given
        byte[] bytes = "content".getBytes(StandardCharsets.UTF_8);
        String expectedFileHash = "0xed7002b439e9ac845f22357d822bac1444730fbdb6016d3ec9432297b9ec9f73";

        // when
        String hexHash = testObj.calculateHash(bytes);

        // then
        assertThat(hexHash).isEqualTo(expectedFileHash);
    }
}