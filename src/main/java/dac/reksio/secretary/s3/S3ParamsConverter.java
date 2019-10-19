package dac.reksio.secretary.s3;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Component
class S3ParamsConverter {

    S3UploadRequest convert(String filename, byte[] fileContent, HttpServletRequest httpRequest) {
        return S3UploadRequest.builder()
                              .key(httpRequest.getParameter("key"))
                              .tagging(httpRequest.getParameter("tagging"))
                              .contentType(httpRequest.getParameter("Content-Type"))
                              .successActionRedirect(httpRequest.getParameter("success_action_redirect"))
                              .amzMetaUuid(httpRequest.getParameter("x-amz-meta-uuid"))
                              .amzMetaTag(httpRequest.getParameter("x-amz-meta-tag"))
                              .awsAccessKeyId(httpRequest.getParameter("AWSAccessKeyId"))
                              .policy(httpRequest.getParameter("Policy"))
                              .submit(httpRequest.getParameter("submit"))
                              .filename(filename)
                              .fileContent(fileContent)
                              .build();
    }
}
