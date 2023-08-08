package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.*;
import com.suite.suite_user_service.member.security.dto.AuthorizerDto;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {

    Token getSuiteToken(ReqSignInMemberDto reqSignInMemberDto, String userAgent, PasswordEncoder passwordEncoder);

    Message getOauthSuiteToken(String accessToken, String userAgent, PasswordEncoder passwordEncoder);
    void saveMemberInfo(ReqSignUpMemberDto reqSignUpMemberDto);
    ResMemberInfoDto getMemberInfo(AuthorizerDto authorizerDto);

    void updateMemberInfo(AuthorizerDto authorizerDto, ReqUpdateMemberDto reqUpdateMemberDto);

    void withdrawalMember(AuthorizerDto authorizerDto);

    public void checkEmail(EmailDto emailDto);

}
