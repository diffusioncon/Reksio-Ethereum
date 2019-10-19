package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.config.s3.S3ConfigRepository;
import dac.reksio.secretary.s3.S3UploadRequest;
import io.minio.MinioClient;
import io.minio.ServerSideEncryption;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReksioMinioClient {

    private final S3ConfigRepository s3ConfigRepository;

    @SneakyThrows
    void uploadFile(S3UploadRequest s3UploadRequest) {
        var s3Configuration = s3ConfigRepository.getOne(S3ConfigRepository.ID);

        MinioClient minioClient = new MinioClient(s3Configuration.getApiUrl(), s3Configuration.getKey(), s3Configuration.getSecret());
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
}
