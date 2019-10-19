package dac.reksio.secretary.files;

import dac.reksio.secretary.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
class FileController {

    private final FileRepository fileRepository;

    @GetMapping
    List<FileEntity> getFiles() {
        return fileRepository.findAll();
    }

    @PostMapping("/{filename}")
    FileEntity verifyFile(@PathVariable String filename) {
        return fileRepository.findByFilename(filename)
                             .orElseThrow(ResourceNotFoundException::new);
    }
}
