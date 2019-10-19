package dac.reksio.secretary.s3.forward.dlt;

import lombok.Value;

@Value
public class DltHashDto {
    private final String result;
    private final String hash;
}
