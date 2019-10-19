package dac.reksio.secretary.s3;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class S3ParamsConverter {

    S3UploadRequest convert(HttpServletRequest httpRequest) {
        return S3UploadRequest.builder()
                              .key(httpRequest.getParameter("key"))
                              .tagging(httpRequest.getParameter("tagging"))
                              .contentType(httpRequest.getParameter("Content-Type"))
                              .successActionRedirect(httpRequest.getParameter("success_action_redirect"))
                              .build();
    }
}
