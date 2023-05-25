package com.jtmnetwork.economy.entrypoint.configuration

import com.jtmnetwork.economy.core.usecase.converter.EconomyAuthenticationConverter
import com.jtmnetwork.economy.data.security.AudienceValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.config.EnableWebFlux

@Configuration
@EnableWebFlux
open class SecurityConfiguration {

    @Value("\${spring.security.oauth2.resourceserver.jwk.issuer-uri}")
    lateinit var issuerUri: String

    @Value("\${auth0.backend_audience}")
    lateinit var audience: String

    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.cors()
            .and()
            .csrf().disable()
            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS).permitAll() // Add protection to endpoints.
            .pathMatchers("/**").permitAll()
            .pathMatchers("/currency/**").permitAll()
            .pathMatchers("/wallet/**").permitAll()
            .pathMatchers("/rate/**").permitAll()
            .pathMatchers("/transaction/**").permitAll()
            .anyExchange().authenticated()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(EconomyAuthenticationConverter())
        return http.build()
    }

    @Bean
    fun jwtDecoder(): ReactiveJwtDecoder {
        val jwtDecoder = ReactiveJwtDecoders.fromOidcIssuerLocation(issuerUri) as NimbusReactiveJwtDecoder
        val audienceValidator = AudienceValidator(audience)
        val withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri)
        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)
        jwtDecoder.setJwtValidator(withAudience)
        return jwtDecoder
    }
}