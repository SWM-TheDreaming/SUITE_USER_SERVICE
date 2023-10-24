package com.suite.suite_user_service.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppleAuthDto {
    private String iss;
    private String aud;
    private String exp;
    private String iat;
    private String sub;
    private String email;
    private String email_verified;
    private String auth_time;
    private boolean nonce_supported;

}
