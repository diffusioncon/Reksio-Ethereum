package dac.reksio.secretary.params;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

@Configuration
@EnableWebMvc
public class ParamsConfiguration implements WebMvcConfigurer {

    private final RenamingProcessor renamingProcessor = new RenamingProcessor();

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(renamingProcessor);
    }

    @Bean
    RenamingProcessor renamingProcessor(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        return renamingProcessor.withRequestMappingHandlerAdapter(requestMappingHandlerAdapter);
    }
}
