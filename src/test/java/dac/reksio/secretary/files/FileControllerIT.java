package dac.reksio.secretary.files;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    void setup() {
        fileRepository.save(FileEntity.builder().filename("file1").hash("hash1").uploadDateTime(Instant.parse("2019-10-19T12:59:00.646819Z")).build());
        fileRepository.save(FileEntity.builder().filename("file2").hash("hash2").uploadDateTime(Instant.parse("2019-10-12T12:59:00.646819Z")).build());
    }

    @Test
    void shouldReturnFiles() throws Exception {
        //when
        //then
        mockMvc.perform(get("/api/files"))
               .andExpect(status().is(HttpStatus.OK.value()))
               .andExpect(jsonPath("$.[0].hash", Matchers.is("hash1")))
               .andExpect(jsonPath("$.[0].filename", Matchers.is("file1")))
               .andExpect(jsonPath("$.[0].uploadDateTime", Matchers.is("2019-10-19T12:59:00.646819Z")))
               .andExpect(jsonPath("$.[1].hash", Matchers.is("hash2")))
               .andExpect(jsonPath("$.[1].filename", Matchers.is("file2")))
               .andExpect(jsonPath("$.[1].uploadDateTime", Matchers.is("2019-10-12T12:59:00.646819Z")));

    }

    @Test
    void shouldReturnFile() throws Exception {
        //when
        //then
        mockMvc.perform(post("/api/files/file1"))
               .andExpect(status().is(HttpStatus.OK.value()))
               .andExpect(jsonPath("$.hash", Matchers.is("hash1")))
               .andExpect(jsonPath("$.filename", Matchers.is("file1")))
               .andExpect(jsonPath("$.uploadDateTime", Matchers.is("2019-10-19T12:59:00.646819Z")));
    }

    @Test
    void shouldReturnNotFound_whenFileNotExists() throws Exception {
        //when
        //then
        mockMvc.perform(post("/api/files/somerole"))
               .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @AfterEach
    void cleanup() {
        fileRepository.deleteAll();
    }

}
