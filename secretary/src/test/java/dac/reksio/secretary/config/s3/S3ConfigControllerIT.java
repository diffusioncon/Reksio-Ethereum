package dac.reksio.secretary.config.s3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class S3ConfigControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private S3ConfigRepository s3ConfigRepository;

    @Test
    void shouldSaveS3Config() throws Exception {
        //given
        String json = "{\n" +
                "\"apiUrl\": \"http://localhost:9001\",\n" +
                "\"bucket\": \"camera1\",\n" +
                "\"key\": \"minio\",\n" +
                "\"secret\": \"minio123\",\n" +
                "\"region\": \"eu-central-1\"\n" +
                "}";

        //when
        mockMvc.perform(put("/api/configs/s3")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        )
               .andExpect(status().is2xxSuccessful());

        //then
        Optional<S3Config> s3Config = s3ConfigRepository.findById(S3ConfigRepository.ID);
        assertThat(s3Config).hasValue(S3Config.builder()
                                              .id(S3ConfigRepository.ID)
                                              .apiUrl("http://localhost:9001")
                                              .bucket("camera1")
                                              .key("minio")
                                              .secret("minio123")
                                              .region("eu-central-1")
                                              .build());
    }

}
