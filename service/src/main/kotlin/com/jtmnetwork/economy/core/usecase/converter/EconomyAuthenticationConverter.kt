package com.jtmnetwork.economy.core.usecase.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono
import java.util.stream.Collectors

class EconomyAuthenticationConverter: Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private val SCOPE_AUTHORITY_PREFIX = "SCOPE_"
    private val WELL_KNOWN_SCOPE_ATTRIBUTE_NAME = listOf("scope", "scp", "authorities", "permissions")

    override fun convert(source: Jwt): Mono<AbstractAuthenticationToken> {
        val authorities = getScopes(source)
            .stream()
            .map { authority -> SCOPE_AUTHORITY_PREFIX + authority }
            .map { SimpleGrantedAuthority(it) }
            .collect(Collectors.toList())
        return Mono.just(JwtAuthenticationToken(source, authorities.orEmpty()))
    }

    private fun getScopes(source: Jwt): Collection<String> {
        val authorities: MutableList<String> = ArrayList()
        for (attribute in WELL_KNOWN_SCOPE_ATTRIBUTE_NAME) {
            val scopes = source.claims[attribute]
            if (scopes is String) {
                val str: String = scopes
                if (StringUtils.hasText(scopes)) authorities.addAll(str.split(" "))
            } else  if (scopes is Collection<*>) {
                scopes.stream().map { it.toString() }.forEach { authorities.add(it) }
            }
        }

        return authorities
    }
}