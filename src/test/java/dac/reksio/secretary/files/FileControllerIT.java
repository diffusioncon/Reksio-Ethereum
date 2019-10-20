package dac.reksio.secretary.files;

import dac.reksio.secretary.s3.forward.dlt.DltHashDto;
import dac.reksio.secretary.s3.forward.dlt.DltService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.when;
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
    @MockBean
    private DltService dltService;
    @MockBean
    private FileHashUpdater fileHashUpdater;

    @BeforeEach
    void setup() {
        fileRepository.save(FileEntity.builder().filename("file1").hash("hash1").uploadDateTime(Instant.parse("2019-10-19T12:59:00.646819Z")).transactionHash("txHash").build());
        fileRepository.save(FileEntity.builder().filename("file2").hash("hash2").uploadDateTime(Instant.parse("2019-10-12T12:59:00.646819Z")).transactionHash("txHash2").build());
        when(dltService.getHashOfFile("file1")).thenReturn(new DltHashDto("OK", "hash1"));
        when(dltService.getHashOfFile("file2")).thenReturn(new DltHashDto("OK", "hash2"));
        when(fileHashUpdater.updateFile(isA(FileEntity.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    void shouldReturnFiles() throws Exception {
        //when
        //then
        mockMvc.perform(get("/api/files"))
               .andExpect(status().is(HttpStatus.OK.value()))
               .andDo(MockMvcResultHandlers.log())
               .andExpect(jsonPath("$.content[0].hash", is("hash1")))
               .andExpect(jsonPath("$.content[0].filename", is("file1")))
               .andExpect(jsonPath("$.content[0].uploadDateTime", is("2019-10-19T12:59:00.646819Z")))
               .andExpect(jsonPath("$.content[0].transactionHash", is("txHash")))
               .andExpect(jsonPath("$.content[1].hash", is("hash2")))
               .andExpect(jsonPath("$.content[1].filename", is("file2")))
               .andExpect(jsonPath("$.content[1].uploadDateTime", is("2019-10-12T12:59:00.646819Z")))
               .andExpect(jsonPath("$.content[1].transactionHash", is("txHash2")));

    }

    @Test
    void shouldReturnFile() throws Exception {
        //when
        //then
        mockMvc.perform(post("/api/files/file1"))
               .andExpect(status().is(HttpStatus.OK.value()))
               .andExpect(jsonPath("$.hash", is("hash1")))
               .andExpect(jsonPath("$.filename", is("file1")))
               .andExpect(jsonPath("$.uploadDateTime", is("2019-10-19T12:59:00.646819Z")));
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
