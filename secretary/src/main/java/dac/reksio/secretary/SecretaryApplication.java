package dac.reksio.secretary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@PropertySource(value = "file:/config/application.properties", ignoreResourceNotFound = true)
public class SecretaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecretaryApplication.class, args);
    }

}
