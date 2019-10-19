package dac.reksio.secretary.files;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Data
class File {
    @Id
    private Long id;
    private String filename;
    private Instant uploadDateTime;
    private String hash;
}
