package dac.reksio.secretary.files;

import dac.reksio.secretary.s3.forward.dlt.DltHashDto;
import dac.reksio.secretary.s3.forward.dlt.DltService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileConverter {
    private final DltService dltService;

    public FileDto convertToDto(FileEntity fileEntity) {
        DltHashDto hashOfFile = dltService.getHashOfFile(fileEntity.getFilename());
        boolean isOk = hashOfFile.getHash().equalsIgnoreCase(fileEntity.getHash());
        return FileDto.builder()
                      .filename(fileEntity.getFilename())
                      .hash(fileEntity.getHash())
                      .uploadDateTime(fileEntity.getUploadDateTime())
                      .hashIsOk(isOk)
                      .build();
    }

}
