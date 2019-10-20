package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.files.FileEntity;
import dac.reksio.secretary.files.FileRepository;
import dac.reksio.secretary.s3.S3UploadRequest;
import dac.reksio.secretary.s3.forward.dlt.DltService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class S3UploadProxy {

    private final FileHashCalculator fileHashCalculator;
    private final DltService dltService;
    private final FileRepository filesRepository;
    private final ReksioStorageClient reksioS3Client;

    public void forwardRequest(S3UploadRequest s3UploadRequest) {
        String hexHash = fileHashCalculator.calculateHash(s3UploadRequest.getFileContent());
        String filename = s3UploadRequest.getKey();

        FileEntity file = saveInDb(filename, hexHash);
        dltService.saveInDlt(file.getId(), filename, hexHash);

        reksioS3Client.uploadFile(s3UploadRequest);
    }

    private FileEntity saveInDb(String filename, String hexHash) {
        return filesRepository.save(FileEntity.builder()
                                              .filename(filename)
                                              .hash(hexHash)
                                              .uploadDateTime(Instant.now())
                                              .build());
    }
}
