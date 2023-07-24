package com.suite.suite_user_service.member.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorizerDto {
    private Long memberId;
    private String email;
    private String name;
    private String nickName;
    private String accountStatus;
    private String role;


    @Builder
    public AuthorizerDto(Long memberId, String email, String name, String nickName, String accountStatus, String role) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.accountStatus = accountStatus;
        this.role = role;
    }

    @Getter
    @AllArgsConstructor
    public enum ClaimName {
        ID("ID"),
        NAME("NAME"),
        NICKNAME("NICKNAME"),
        ACCOUNTSTATUS("ACCOUNTSTATUS"),
        ROLE("ROLE");
        private final String value;
    }
}
