package com.suite.suite_user_service.member.dto;

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

    @Builder
    public AuthorizerDto(Long memberId, String email, String name, String nickName, String accountStatus) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.accountStatus = accountStatus;
    }
}