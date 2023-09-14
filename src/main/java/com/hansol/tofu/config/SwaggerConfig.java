package com.hansol.tofu.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(title = "두부한모 API 명세서",
                description = "두부한모(tofu-hansol) API 명세서",
                version = "v1"))
@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    scheme = "bearer",
    bearerFormat = "JWT",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER,
    paramName = HttpHeaders.AUTHORIZATION
)
public class SwaggerConfig {
}
