package com.jtmnetwork.economy.core.usecase.converter;

import io.netty.util.internal.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EconomyAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private final String SCOPE_AUTHORITY_PREFIX = "SCOPE_";
    private final List<String> WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES = List.of("scope", "scp", "authorities", "roles", "permissions");

    @Override
    public Mono<AbstractAuthenticationToken> convert(@NotNull Jwt source) {
        List<GrantedAuthority> authorities = getScopes(source)
                .stream()
                .map(authority -> SCOPE_AUTHORITY_PREFIX + authority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return Mono.just(new JwtAuthenticationToken(source, authorities));
    }

    private List<String> getScopes(Jwt source) {
        List<String> authorities = new ArrayList<>();
        for (String attribute : WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES) {
            Object scopes = source.getClaims().get(attribute);
            if (scopes.getClass() == String.class) {
                String str = (String) scopes;
                if (StringUtils.hasText(str)) authorities.addAll(List.of(str.split(" ")));
            } else if (scopes instanceof Collection<?> coll) {
                coll.stream().map(Object::toString).filter(item -> !StringUtil.isNullOrEmpty(item)).forEach(authorities::add);
            }
        }

        return authorities;
    }
}
