package dac.reksio.secretary.config.dlt;

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
class DltControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DltConfigRepository dltConfigRepository;

    @Test
    void shouldSaveDltConfig() throws Exception {
        //given
        String json = "{\"dlt\":\"ETHEREUM\"}";

        //when
        mockMvc.perform(put("/api/configs/dlt")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        )
               .andExpect(status().is2xxSuccessful());

        //then
        Optional<DltConfig> dltConfig = dltConfigRepository.findById(DltConfigRepository.ID);
        assertThat(dltConfig).hasValueSatisfying(
                config -> assertThat(config.getDlt()).isEqualTo(Dlt.ETHEREUM)
        );
    }

}
