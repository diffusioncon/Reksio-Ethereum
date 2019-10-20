package dac.reksio.secretary.files;

import com.google.common.net.HttpHeaders;
import dac.reksio.secretary.exception.ResourceNotFoundException;
import dac.reksio.secretary.s3.forward.ReksioStorageClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
class FileController {

    private final FileRepository fileRepository;
    private final FileHashUpdater fileHashUpdater;
    private final FileConverter fileConverter;
    private final ReksioStorageClient reksioStorageClient;

    @GetMapping
    Page<FileDto> getFiles(Pageable pageable) {
        return fileRepository.findAll(pageable)
                             .map(this::convertToDto);
    }

    @GetMapping("/{filename}")
    void getFile(@PathVariable String filename, HttpServletResponse response) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        byte[] fileContent = reksioStorageClient.getFileContent(filename);
        try (var os = response.getOutputStream()) {
            os.write(fileContent);
        }
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
