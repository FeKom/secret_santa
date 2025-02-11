package com.github.fekom.secret_santa.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement() .addList("Bearer Authentication"))
            .components(new Components().addSecuritySchemes("Bearer Authentication", createApiKeyScheme()))
            .info(new Info()
                .title("Secret-santa")
                .description("Swagger for a Secret-santa API")
                .version("1.0")
                .description("Secret Santa API")
                );
                
    }

    private SecurityScheme createApiKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                    .bearerFormat("JWT")
                    .scheme("bearer");
    }

}
