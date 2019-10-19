package dac.reksio.secretary.s3;

import dac.reksio.secretary.params.ParamName;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class S3UploadRequest {

    private final String key;
    private final String tagging;
    @ParamName("success_action_redirect")
    private final String successActionRedirect;
    @ParamName("Content-Type")
    private final String contentType;
}
