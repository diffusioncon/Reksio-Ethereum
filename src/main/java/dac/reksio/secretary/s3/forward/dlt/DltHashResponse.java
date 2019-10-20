package dac.reksio.secretary.s3.forward.dlt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class DltHashResponse {
    private String result;
    @JsonProperty("tx_hash")
    private String transactionHash;
}
