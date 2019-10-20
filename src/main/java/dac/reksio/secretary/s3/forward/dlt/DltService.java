package dac.reksio.secretary.s3.forward.dlt;

import dac.reksio.secretary.files.FileEntity;
import dac.reksio.secretary.files.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DltService {

    private final DltClient dltClient;
    private final FileRepository fileRepository;

    @Async
    public void saveInDlt(Long fileId, String originalFilename, String hexHash) {
        DltHashResponse dltHashResponse = dltClient.saveInDlt(originalFilename, hexHash);
        FileEntity fileEntity = fileRepository.getOne(fileId);
        fileEntity.setTransactionHash(dltHashResponse.getTransactionHash());
        fileRepository.save(fileEntity);
    }

    public DltHashDto getHashOfFile(String filename) {
        return dltClient.getHashOfFile(filename);
    }

}
