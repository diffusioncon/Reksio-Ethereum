package dac.reksio.secretary.s3.forward.dlt;

import dac.reksio.secretary.files.FileEntity;
import dac.reksio.secretary.files.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DltHashProvider {

    private final DltClient dltClient;
    private final FileRepository fileRepository;

    public DltHashDto getHashOfFile(FileEntity file) {
        DltHashDto dltHashDto = dltClient.getHashOfFile(file.getFilename());
        FileEntity fileEntity = fileRepository.getOne(file.getId());
        fileEntity.setHashCalculationDateTime(Instant.now());
        fileRepository.save(fileEntity);
        return dltHashDto;
    }
}
