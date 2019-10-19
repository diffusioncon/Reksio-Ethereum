package dac.reksio.secretary.config.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/configs/s3")
class S3ConfigController {

    private final S3ConfigSaver s3ConfigSaver;

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@RequestBody S3ConfigDTO s3ConfigDTO) {
        s3ConfigSaver.save(s3ConfigDTO);
    }

}
