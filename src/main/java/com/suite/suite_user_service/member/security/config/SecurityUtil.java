package com.suite.suite_user_service.member.security.config;

import com.suite.suite_user_service.member.dto.AccountStatus;
import com.suite.suite_user_service.member.dto.AuthorizerDto;

import com.suite.suite_user_service.member.entity.Member;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import com.suite.suite_user_service.member.security.JwtAuthenticationToken;
import com.suite.suite_user_service.member.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {
    public static AuthorizerDto getSuiteAuthorizer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        if (authentication instanceof JwtAuthenticationToken) {
            Claims claims = ((JwtAuthenticationToken) authentication).getClaims();



            return AuthorizerDto.builder()
                    .memberId(Long.parseLong((String) claims.get(JwtTokenProvider.ID)))
                    .email(authentication.getName())
                    .name((String) claims.get(JwtTokenProvider.NAME))
                    .nickName((String) claims.get(JwtTokenProvider.NICKNAME))
                    .accountStatus((String) claims.get(JwtTokenProvider.ACCOUNTSTATUS)).build();

        }
        return null;
    }
}
