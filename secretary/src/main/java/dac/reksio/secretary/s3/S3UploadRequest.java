package dac.reksio.secretary.s3;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
@RequiredArgsConstructor
public class S3UploadRequest {
    private final String key;
    private final String tagging;
    private final String successActionRedirect;
    private final String contentType;
    private final String amzMetaUuid;
    private final String amzMetaTag;
    private final String awsAccessKeyId;
    private final String policy;
    private final String submit;
    private final byte[] fileContent;
}
