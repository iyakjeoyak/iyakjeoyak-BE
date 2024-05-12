package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class Swagger3Config {
    private static final String SECURITY_SCHEME_NAME = "authorization";    // 추가


    private String serverStartTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("IYJY swagger")
                .version("1.0")
                .description("<li>서비스 가동 일시 : " + serverStartTime + "</li>");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)
                )
                .info(info);
    }
}