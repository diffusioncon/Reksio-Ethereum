package dac.reksio.secretary.s3.forward.dlt;

import dac.reksio.secretary.files.FileConverter;
import dac.reksio.secretary.files.FileEntity;
import dac.reksio.secretary.files.FileRepository;
import dac.reksio.secretary.files.FileWebsocketSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DltSaver {

    private final DltClient dltClient;
    private final FileRepository fileRepository;
    private final FileWebsocketSender fileWebsocketSender;
    private final FileConverter fileConverter;

    @Async
    @Transactional
    public void saveInDlt(Long fileId, String originalFilename, String hexHash) {
        DltHashResponse dltHashResponse = dltClient.saveInDlt(originalFilename, hexHash);
        FileEntity fileEntity = fileRepository.getOne(fileId);
        fileEntity.setTransactionHash(dltHashResponse.getTransactionHash());
        sendToWebsocket(fileRepository.save(fileEntity));
    }

    private void sendToWebsocket(FileEntity file) {
        fileWebsocketSender.sendFile(fileConverter.convertToDto(file));
    }

}
