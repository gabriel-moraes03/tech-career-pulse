package com.moraes.tech_career_pulse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tech Career Pulse API")
                        .description("API para plataforma de inteligência de carreira com IA Generativa.")
                        .contact(new Contact()
                                .name("Gabriel Moraes")
                                .email("moraesgabriel0003@gmail.com"))
                        .version("1.0.0"));
    }
}
