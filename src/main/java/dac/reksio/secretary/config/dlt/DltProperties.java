package dac.reksio.secretary.config.dlt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class DltProperties {

    @Value("dlt.ethereum.uri")
    private String ethereumUri;

    private Map<Dlt, String> uriMap;

    @PostConstruct
    public void init() {
        uriMap = Map.of(
                Dlt.ETHEREUM, ethereumUri
        );
    }

    public String getUri(Dlt dlt) {
        return uriMap.get(dlt);
    }
}