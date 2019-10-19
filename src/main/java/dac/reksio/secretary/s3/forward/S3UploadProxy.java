package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.files.FileEntity;
import dac.reksio.secretary.files.FilesRepository;
import dac.reksio.secretary.s3.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class S3UploadProxy {

    private final FileHashCalculator fileHashCalculator;
    private final DltClient dltClient;
    private final FilesRepository filesRepository;
    private final ReksioS3Client reksioS3Client;

    public PutObjectResponse forwardRequest(S3UploadRequest s3UploadRequest) {
        String hexHash = fileHashCalculator.calculateHash(s3UploadRequest.getFile());
        String filename = s3UploadRequest.getKey();
        dltClient.saveInDlt(filename, hexHash);

        filesRepository.save(FileEntity.builder()
                                       .filename(filename)
                                       .hash(hexHash)
                                       .uploadDateTime(Instant.now())
                                       .build());

        return reksioS3Client.uploadFile(s3UploadRequest);
    }
}
