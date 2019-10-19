package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.config.s3.S3ConfigRepository;
import dac.reksio.secretary.s3.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.net.URI;

@RequiredArgsConstructor
class ReksioS3Client implements ReksioStorageClient {

    private final S3ConfigRepository s3ConfigRepository;

    @Override
    public void uploadFile(S3UploadRequest s3UploadRequest) {
        var s3Configuration = s3ConfigRepository.getOne(S3ConfigRepository.ID);
        var credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(s3Configuration.getKey(), s3Configuration.getSecret())
        );
        S3Configuration build = S3Configuration.builder()
                                               .pathStyleAccessEnabled(true)
                                               .build();

        var client = S3Client.builder()
                             .endpointOverride(URI.create(s3Configuration.getApiUrl()))
                             .serviceConfiguration(build)
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
        client.putObject(putObjectRequest, requestBody);
    }

    @Override
    public byte[] getFileContent(String filename) {
        return new byte[0];
    }

    private RequestBody getRequestBody(S3UploadRequest s3UploadRequest) {
        byte[] bytes = s3UploadRequest.getFileContent();
        return RequestBody.fromContentProvider(() -> new ByteArrayInputStream(bytes), bytes.length, s3UploadRequest.getContentType());
    }
}
