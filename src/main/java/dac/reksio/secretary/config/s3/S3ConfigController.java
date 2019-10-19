package dac.reksio.secretary.config.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/configs/s3")
class S3ConfigController {

    private final S3ConfigSaver s3ConfigSaver;

    @PutMapping
    public ResponseEntity save(@RequestBody S3ConfigDTO s3ConfigDTO) {
        s3ConfigSaver.save(s3ConfigDTO);
        return ResponseEntity.ok().build();
    }

}
