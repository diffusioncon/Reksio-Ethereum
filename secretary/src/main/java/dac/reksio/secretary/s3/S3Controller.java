package dac.reksio.secretary.s3;

import dac.reksio.secretary.s3.forward.S3UploadProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3ParamsConverter s3ParamsConverter;
    private final S3UploadProxy s3UploadProxy;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest httpRequest) throws IOException {
        S3UploadRequest s3UploadRequest = s3ParamsConverter.convert(file.getBytes(), httpRequest);

        s3UploadProxy.forwardRequest(s3UploadRequest);
    }
}
