package dac.reksio.secretary.s3.forward.dlt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class DltFileDto {

    @JsonProperty("file_id")
    private final String fileId;
    @JsonProperty("file_hash")
    private final String fileHash;
}
