package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.config.s3.S3Config;
import dac.reksio.secretary.config.s3.S3ConfigRepository;
import dac.reksio.secretary.s3.S3UploadRequest;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
class ReksioMinioClient implements ReksioStorageClient {

    private final S3ConfigRepository s3ConfigRepository;

    @Override
    @SneakyThrows
    public void uploadFile(S3UploadRequest s3UploadRequest) {
        var s3Configuration = s3ConfigRepository.getOne(S3ConfigRepository.ID);

        MinioClient minioClient = getMinioClient(s3Configuration);
        minioClient.putObject(
                s3Configuration.getBucket(),
                s3UploadRequest.getKey(),
                new ByteArrayInputStream(s3UploadRequest.getFileContent()),
                (long) s3UploadRequest.getFileContent().length,
                new HashMap<>(),
                null,
                s3UploadRequest.getContentType()
        );
    }

    @Override
    @SneakyThrows
    public byte[] getFileContent(String filename) {
        var s3Configuration = s3ConfigRepository.getOne(S3ConfigRepository.ID);

        MinioClient minioClient = getMinioClient(s3Configuration);
        try (InputStream inputStream = minioClient.getObject(s3Configuration.getBucket(), filename)) {
            return inputStream.readAllBytes();
        }
    }

    private MinioClient getMinioClient(S3Config s3Configuration) throws InvalidEndpointException, InvalidPortException {
        return new MinioClient(s3Configuration.getApiUrl(), s3Configuration.getKey(), s3Configuration.getSecret());
    }
}
