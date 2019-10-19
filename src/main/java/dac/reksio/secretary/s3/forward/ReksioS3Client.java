package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.config.s3.S3ConfigRepository;
import dac.reksio.secretary.s3.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ReksioS3Client {

    private final S3ConfigRepository s3ConfigRepository;

    @SneakyThrows
    PutObjectResponse uploadFile(S3UploadRequest s3UploadRequest) {
        var s3Configuration = s3ConfigRepository.getOne(S3ConfigRepository.ID);
        var credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(s3Configuration.getKey(), s3Configuration.getSecret())
        );
        var client = S3Client.builder()
                             .credentialsProvider(credentialsProvider)
                             .region(Region.of(s3Configuration.getRegion()))
                             .build();
        var putObjectRequest = PutObjectRequest.builder()
                                               .bucket(s3Configuration.getBucket())
                                               .key(s3UploadRequest.getKey())
                                               .websiteRedirectLocation(s3UploadRequest.getSuccessActionRedirect())
                                               .contentType(s3UploadRequest.getContentType())
                                               .build();

        RequestBody requestBody = getRequestBody(s3UploadRequest);
        return client.putObject(putObjectRequest, requestBody);
    }

    private RequestBody getRequestBody(S3UploadRequest s3UploadRequest) throws IOException {
        byte[] bytes = s3UploadRequest.getFile().getBytes();
        return RequestBody.fromContentProvider(() -> new ByteArrayInputStream(bytes), bytes.length, s3UploadRequest.getContentType());
    }
}
