package dac.reksio.secretary.s3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest
@AutoConfigureMockMvc
class S3ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldUploadFileToS3() throws Exception {
        // given
        var multipart = multipart("/")
                .param("key", "acl")
                .param("tagging", "<Tagging><TagSet><Tag><Key>Tag Name</Key><Value>Tag Value</Value></Tag></TagSet></Tagging>")
                .param("success_action_redirect", "success_action_redirect")
                .param("Content-Type", "video");

        // when-then
        mockMvc.perform(multipart)
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
