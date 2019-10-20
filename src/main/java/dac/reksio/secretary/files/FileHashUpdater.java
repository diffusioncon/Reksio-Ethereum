package dac.reksio.secretary.files;

import dac.reksio.secretary.s3.forward.FileHashCalculator;
import dac.reksio.secretary.s3.forward.ReksioStorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
class FileHashUpdater {

    private final FileHashCalculator fileHashCalculator;
    private final FileRepository fileRepository;
    private final ReksioStorageClient reksioStorageClient;

    FileEntity updateFile(FileEntity fileEntity) {
        byte[] fileContent = reksioStorageClient.getFileContent(fileEntity.getFilename());
        String hash = fileHashCalculator.calculateHash(fileContent);
        fileEntity.setHash(hash);
        fileEntity.setHashCalculationDateTime(Instant.now());
        return fileRepository.save(fileEntity);
    }
}
