package dac.reksio.secretary.s3.forward;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
class FileHashCalculator {

    private final HashFunction sha256 = Hashing.sha256();

    @SneakyThrows
    String calculateHash(MultipartFile multipartFile) {
        byte[] bytes = multipartFile.getBytes();
        return sha256.hashBytes(bytes).toString();
    }
}
