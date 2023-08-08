package com.suite.suite_user_service.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class ReqSignInMemberDto {

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%#?&])[A-Za-z\\d@$!%*#?&]{10,}$")
    private String password;

    private boolean isAuth;
    @Builder
    public ReqSignInMemberDto(String email, String password, boolean isAuth) {
        this.email = email;
        this.password = password;
        this.isAuth = isAuth;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
