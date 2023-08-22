package com.suite.suite_user_service.member.dto;

import com.suite.suite_user_service.member.entity.Member;
import com.suite.suite_user_service.member.entity.MemberInfo;
import com.suite.suite_user_service.member.handler.CustomException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class ReqSignUpMemberDto {

    @Email(message = "Invalid email format")
    private String email;

    //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%#?&])[A-Za-z\\d@$!%*#?&]{10,}$")
    private String password;

    private String role;

    @Pattern(regexp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣].{0,3}$")
    private String name;

    @Pattern(regexp = "^.{1,8}$")
    private String nickName;

    @Pattern(regexp = "^01[0-9]{1}-[0-9]{4}-[0-9]{4}$")
    private String phone;

    @Pattern(regexp = "^\\d{6}-\\d{1}$")
    private String securityNum;

    private StudyCategory preferStudy;

    private StudyType studyMethod;
    private boolean isOauth;
    private String profileImage;

    @Builder
    public ReqSignUpMemberDto(String email, String password, String role, String name, String nickName, String phone, String securityNum, StudyCategory preferStudy, StudyType studyMethod, boolean isOauth, String profileImage) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.nickName = nickName;
        this.phone = phone;
        this.securityNum = securityNum;
        this.preferStudy = preferStudy;
        this.studyMethod = studyMethod;
        this.isOauth = isOauth;
        this.profileImage = profileImage;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
    public Member toMemberEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .role(Role.from(role))
                .isOauth(isOauth)
                .accountStatus(AccountStatus.ACTIVATE.getStatus()).build();
    }

    public MemberInfo toMemberInfoEntity() {
        return MemberInfo.builder()
                .name(name)
                .nickname(nickName)
                .phone(phone)
                .securityNum(securityNum)
                .preferStudy(preferStudy)
                .studyMethod(studyMethod).build();
    }

}
