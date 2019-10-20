package dac.reksio.secretary.files;

import dac.reksio.secretary.s3.forward.dlt.DltClient;
import dac.reksio.secretary.s3.forward.dlt.DltHashDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileConverter {

    private final DltClient dltClient;

    public FileDto convertToDto(FileEntity fileEntity) {
        DltHashDto hashOfFile = dltClient.getHashOfFile(fileEntity.getFilename());
        boolean isOk = hashOfFile.getHash().equalsIgnoreCase(fileEntity.getHash());
        return FileDto.builder()
                      .filename(fileEntity.getFilename())
                      .hash(fileEntity.getHash())
                      .uploadDateTime(fileEntity.getUploadDateTime())
                      .hashIsOk(isOk)
                      .build();
    }

}
