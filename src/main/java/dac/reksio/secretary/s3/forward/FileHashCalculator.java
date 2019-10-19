package dac.reksio.secretary.s3.forward;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import dac.reksio.secretary.s3.S3UploadRequest;
import org.springframework.stereotype.Component;

@Component
public class FileHashCalculator {

    private final HashFunction sha256 = Hashing.sha256();

    public String calculateHash(byte[] bytes) {
        return "0x" + sha256.hashBytes(bytes).toString();
    }
}
