package dac.reksio.secretary.config.dlt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/configs/dlt")
class DltConfigController {

    private final DltConfigSaver dltConfigSaver;

    @PutMapping
    public ResponseEntity save(@RequestBody DltConfigDTO dltConfigDTO) {
        dltConfigSaver.save(dltConfigDTO);
        return ResponseEntity.ok().build();
    }

}
