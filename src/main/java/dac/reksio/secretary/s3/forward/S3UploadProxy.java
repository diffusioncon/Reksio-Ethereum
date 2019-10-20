package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.files.FileEntity;
import dac.reksio.secretary.files.FileRepository;
import dac.reksio.secretary.s3.S3UploadRequest;
import dac.reksio.secretary.s3.forward.dlt.DltSaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3UploadProxy {

    private final FileHashCalculator fileHashCalculator;
    private final DltSaver dltSaver;
    private final FileRepository filesRepository;
    private final ReksioStorageClient reksioS3Client;

    public void forwardRequest(S3UploadRequest s3UploadRequest) {
        String hexHash = fileHashCalculator.calculateHash(s3UploadRequest.getFileContent());
        String filename = s3UploadRequest.getKey();

        FileEntity file = saveInDb(filename, hexHash);
        dltSaver.saveInDlt(file.getId(), filename, hexHash);
        reksioS3Client.uploadFile(s3UploadRequest);
    }

    private FileEntity saveInDb(String filename, String hexHash) {
        Instant now = Instant.now();
        return filesRepository.save(FileEntity.builder()
                                              .filename(filename)
                                              .hash(hexHash)
                                              .hashCalculationDateTime(now)
                                              .uploadDateTime(now)
                                              .build());
    }

}
