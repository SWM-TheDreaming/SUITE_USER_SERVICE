package com.suite.suite_user_service.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class ReqLookupPasswordDto {

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%#?&])[A-Za-z\\d@$!%*#?&]{10,}$")
    private String newPassword;


    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.newPassword = passwordEncoder.encode(this.newPassword);
    }
}
