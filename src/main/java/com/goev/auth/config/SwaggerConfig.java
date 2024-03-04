package com.goev.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
@Slf4j
public class SwaggerConfig {


    @Bean
    public OpenAPI openAPI() {
        List<SecurityRequirement> securityRequirementList = Collections.singletonList(new SecurityRequirement().addList("Bearer"));
        return new OpenAPI()
                .info(new Info().title("Auth API")
                        .description("Auth application")
                        .version("v0.0.1"))
                .components(new Components().addSecuritySchemes("Bearer", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .description("Enter Bearer Token")
                        .scheme("bearer")))
                .security(securityRequirementList)
                .externalDocs(new ExternalDocumentation()
                        .description("Auth Dashboard"));
    }
}
