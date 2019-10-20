package dac.reksio.secretary.config.dlt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/configs/dlt")
class DltConfigController {

    private final DltConfigSaver dltConfigSaver;

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@RequestBody DltConfigDTO dltConfigDTO) {
        dltConfigSaver.save(dltConfigDTO);
    }

}
