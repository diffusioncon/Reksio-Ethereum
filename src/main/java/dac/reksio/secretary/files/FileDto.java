package dac.reksio.secretary.files;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class FileDto {
    private String filename;
    private Instant uploadDateTime;
    private String hash;
    private boolean hashIsOk;
    private String transactionHash;
    private Instant hashCalculationDateTime;
}
