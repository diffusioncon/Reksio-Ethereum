package dac.reksio.secretary.files;

import dac.reksio.secretary.s3.forward.dlt.DltHashDto;
import dac.reksio.secretary.s3.forward.dlt.DltHashProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileConverter {
    private final DltHashProvider dltHashProvider;

    public FileDto convertToDto(FileEntity fileEntity) {
        DltHashDto hashOfFile = dltHashProvider.getHashOfFile(fileEntity);
        boolean isOk = hashOfFile.getHash().equalsIgnoreCase(fileEntity.getHash());
        return FileDto.builder()
                      .filename(fileEntity.getFilename())
                      .hash(fileEntity.getHash())
                      .uploadDateTime(fileEntity.getUploadDateTime())
                      .hashIsOk(isOk)
                      .transactionHash(fileEntity.getTransactionHash())
                      .hashCalculationDateTime(fileEntity.getHashCalculationDateTime())
                      .build();
    }

}
