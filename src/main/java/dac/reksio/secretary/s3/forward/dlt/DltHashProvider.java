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

    public DltHashDto getHashOfFile(FileEntity file) {
        return dltClient.getHashOfFile(file.getFilename());
    }
}
