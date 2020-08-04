package meet_eat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import meet_eat.data.ObjectJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectJsonParser objectJsonParser = new ObjectJsonParser();
        return objectJsonParser.getObjectMapper();
    }
}
