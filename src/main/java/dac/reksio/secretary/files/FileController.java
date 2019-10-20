package dac.reksio.secretary.files;

import dac.reksio.secretary.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
class FileController {

    private final FileRepository fileRepository;
    private final FileHashUpdater fileHashUpdater;
    private final FileConverter fileConverter;

    @GetMapping
    Page<FileDto> getFiles(Pageable pageable) {
        return fileRepository.findAll(pageable)
                             .map(this::convertToDto);
    }

    @PostMapping("/{filename}")
    FileDto verifyFile(@PathVariable String filename) {
        FileEntity fileEntity = fileRepository.findByFilename(filename)
                                              .orElseThrow(ResourceNotFoundException::new);
        FileEntity updatedFile = fileHashUpdater.updateFile(fileEntity);
        return convertToDto(updatedFile);
    }

    private FileDto convertToDto(FileEntity file) {
        return fileConverter.convertToDto(file);
    }
}
