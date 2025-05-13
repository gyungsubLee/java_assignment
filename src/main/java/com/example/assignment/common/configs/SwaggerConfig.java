package com.example.assignment.common.configs;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes("bearer-key", createBearerScheme()))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }

    private Info apiInfo() {
        return new Info()
                .title("Assignment API")
                .description("Spring Boot + JWT 기반 사용자/관리자 인증 시스템")
                .version("v1.0.0");
    }

    private SecurityScheme createBearerScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer") // 인증 타입: Bearer
                .bearerFormat("JWT") // JWT 명시
                .in(SecurityScheme.In.HEADER)
                .name("Authorization"); // 헤더 이름
    }
}