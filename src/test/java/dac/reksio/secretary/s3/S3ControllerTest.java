package dac.reksio.secretary.s3;

import dac.reksio.secretary.files.FileConverter;
import dac.reksio.secretary.files.FileWebsocketSender;
import dac.reksio.secretary.s3.forward.ReksioStorageClient;
import dac.reksio.secretary.s3.forward.dlt.DltClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest
@AutoConfigureMockMvc
class S3ControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReksioStorageClient reksioStorageClient;
    @MockBean
    private DltClient dltClient;
    @MockBean
    private FileWebsocketSender fileWebsocketSender;
    @MockBean
    private FileConverter fileConverter;

    @Test
    void shouldUploadFileToS3() throws Exception {
        // given
        var multipart = multipart("/")
                .file(new MockMultipartFile("file", "s3.txt",
                        "text/plain", "s3 file".getBytes()))
                .param("key", "acl")
                .param("tagging", "<Tagging><TagSet><Tag><Key>Tag Name</Key><Value>Tag Value</Value></Tag></TagSet></Tagging>")
                .param("success_action_redirect", "success_action_redirect")
                .param("Content-Type", "video")
                .param("x-amz-meta-uuid", "uuid")
                .param("x-amz-meta-tag", "metadata")
                .param("AWSAccessKeyId", "access-key-id")
                .param("Policy", "encoded_policy")
                .param("Signature", "signature=")
                .param("submit", "Upload to Amazon S3");

        // when-then
        mockMvc.perform(multipart)
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
