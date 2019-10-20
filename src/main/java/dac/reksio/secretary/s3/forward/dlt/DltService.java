package dac.reksio.secretary.s3.forward.dlt;

import dac.reksio.secretary.files.FileEntity;
import dac.reksio.secretary.files.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DltService {

    private final DltClient dltClient;
    private final FileRepository fileRepository;

    @Async
    @Transactional
    public void saveInDlt(Long fileId, String originalFilename, String hexHash) {
        DltHashResponse dltHashResponse = dltClient.saveInDlt(originalFilename, hexHash);
        FileEntity fileEntity = fileRepository.getOne(fileId);
        fileEntity.setTransactionHash(dltHashResponse.getTransactionHash());
        fileRepository.save(fileEntity);
    }

    public DltHashDto getHashOfFile(FileEntity file) {
        DltHashDto dltHashDto = dltClient.getHashOfFile(file.getFilename());
        FileEntity fileEntity = fileRepository.getOne(file.getId());
        fileEntity.setHashCalculationDateTime(Instant.now());
        fileRepository.save(fileEntity);
        return dltHashDto;
    }

}
