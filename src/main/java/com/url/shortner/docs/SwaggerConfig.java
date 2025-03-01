package com.url.shortner.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI()
                .info(
                        new Info().title("URL Shortner API")
                                .version("v1.0")
                                .description("Tribhuvan nath")
                )
                .servers(List.of(new Server().url("https://localhost:8080").description("local")
                        , new Server().url("http://localhost:8082").description("live"))
                )
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .components(new Components().addSecuritySchemes(
                        "Bearer", new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER).name("Authorization")
                ));
    }
}
