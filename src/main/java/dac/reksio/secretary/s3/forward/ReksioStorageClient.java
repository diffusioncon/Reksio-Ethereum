package dac.reksio.secretary.s3.forward;

import dac.reksio.secretary.s3.S3UploadRequest;

public interface ReksioStorageClient {
    void uploadFile(S3UploadRequest s3UploadRequest);

    byte[] getFileContent(String filename);
}
