package dac.reksio.secretary.files;

import dac.reksio.secretary.exception.ResourceNotFoundException;
import dac.reksio.secretary.s3.forward.dlt.DltHashDto;
import dac.reksio.secretary.s3.forward.dlt.DltService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
class FileController {

    private final FileRepository fileRepository;
    private final DltService dltService;
    private final FileHashUpdater fileHashUpdater;

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

    private FileDto convertToDto(FileEntity fileEntity) {
        DltHashDto hashOfFile = dltService.getHashOfFile(fileEntity.getFilename());
        boolean isOk = hashOfFile.getHash().equalsIgnoreCase(fileEntity.getHash());
        return FileDto.builder()
                      .filename(fileEntity.getFilename())
                      .hash(fileEntity.getHash())
                      .uploadDateTime(fileEntity.getUploadDateTime())
                      .hashIsOk(isOk)
                      .build();
    }
}
