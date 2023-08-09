package com.suite.suite_user_service.member.security;

import com.suite.suite_user_service.member.config.ConfigUtil;
import com.suite.suite_user_service.member.security.dto.AuthorizerDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JwtInfoExtractor {

    @Value("${jwt.access.key}")
    private String accessKey;

    public Claims extractToken(String token) {
        return Jwts.parser().setSigningKey(accessKey.getBytes()).parseClaimsJws(token).getBody();
    }

    public static AuthorizerDto getSuiteAuthorizer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            Claims claims = ((JwtAuthenticationToken) authentication).getClaims();



            return AuthorizerDto.builder()
                    .memberId(Long.parseLong(claims.get(AuthorizerDto.ClaimName.ID.getValue()).toString()))
                    .email(authentication.getName())
                    .name(claims.get(AuthorizerDto.ClaimName.NAME.getValue()).toString())
                    .nickName(claims.get(AuthorizerDto.ClaimName.NICKNAME.getValue()).toString())
                    .accountStatus(claims.get(AuthorizerDto.ClaimName.ACCOUNTSTATUS.getValue()).toString())
                    .role(AuthorizerDto.ClaimName.ROLE.getValue())
                    .build();

        }
        return null;
    }
}
