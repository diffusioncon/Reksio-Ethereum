package dac.reksio.secretary.s3.forward.dlt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DltHashDto {
    private String result;
    private String hash;
}
