package com.suite.suite_user_service.member.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Claims claims;

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Claims claims) {
        super(principal, credentials, authorities);
        this.claims = claims;
    }

    public Claims getClaims() {
        return claims;
    }
}