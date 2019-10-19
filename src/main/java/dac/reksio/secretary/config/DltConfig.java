package dac.reksio.secretary.config;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class DltConfig {
    @Id
    private String id = "dlt";
    private DLT dlt;
}
