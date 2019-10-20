package dac.reksio.secretary.files;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileWebsocketSender {

    private static final String FILES_ENDPOINT = "/api/files";

    private final SimpMessagingTemplate messagingTemplate;

    public void sendFile(FileDto fileDto) {
        messagingTemplate.convertAndSend(FILES_ENDPOINT, fileDto);
    }

}
