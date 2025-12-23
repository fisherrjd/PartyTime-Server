package rip.jade.partytimeserverjava.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI partyTimeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PartyTime Server API")
                        .description("REST API for managing drop parties in OSRS. Track and manage high-value item drops across different game worlds.")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("PartyTime Server")
                                .url("https://github.com/jade/partytime-server")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8000")
                                .description("Local Development Server")
                ));
    }
}
