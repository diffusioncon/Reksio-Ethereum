package dac.reksio.secretary.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3ParamsConverter s3ParamsConverter;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(HttpServletRequest httpRequest) {
        S3UploadRequest s3UploadRequest = s3ParamsConverter.convert(httpRequest);

        log.info("Request: {}", s3UploadRequest);
    }
}
