package dac.reksio.secretary.s3.forward;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import dac.reksio.secretary.s3.S3UploadRequest;
import org.springframework.stereotype.Component;

@Component
class FileHashCalculator {

    private final HashFunction sha256 = Hashing.sha256();

    String calculateHash(S3UploadRequest s3UploadRequest) {
        byte[] bytes = s3UploadRequest.getFileContent();
        return "0x" + sha256.hashBytes(bytes).toString();
    }
}
