package com.jtmnetwork.economy.entrypoint.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@EnableWebFlux
@Configuration
open class CorsConfiguration: WebFluxConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("https://localhost:3000", "https://play.jtm-network.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(7200);
    }
}