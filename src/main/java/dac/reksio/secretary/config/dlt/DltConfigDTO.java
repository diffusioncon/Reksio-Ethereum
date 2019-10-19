package dac.reksio.secretary.config.dlt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DltConfigDTO {
    private Dlt dlt;
    private String uri;
}
