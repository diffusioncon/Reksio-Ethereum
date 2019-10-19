package dac.reksio.secretary.s3;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class S3UploadRequest {

    private final String key;
    private final String tagging;
    private final String successActionRedirect;
    private final String contentType;
}
