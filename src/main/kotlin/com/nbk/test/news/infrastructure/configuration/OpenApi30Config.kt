package com.nbk.test.news.infrastructure.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApi30Config {

    // enable swagger ui to show authorize button using bearer token
    @Bean
    fun customOpenAPI(): OpenAPI? {
        return OpenAPI().components(
            Components()
                .addSecuritySchemes(
                    "bearer-key",
                    SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
            )
        )
    }
}
