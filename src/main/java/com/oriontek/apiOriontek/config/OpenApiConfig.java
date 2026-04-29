package com.oriontek.apiOriontek.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OrionTek API")
                        .description("API REST para gestión de clientes y sus direcciones")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("OrionTek")
                                .email("dev@oriontek.com")));
    }
}