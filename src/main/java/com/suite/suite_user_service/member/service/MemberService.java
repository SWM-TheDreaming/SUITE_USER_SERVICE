package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.*;
import com.suite.suite_user_service.member.security.dto.AuthorizerDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface MemberService {

    Token getSuiteToken(ReqSignInMemberDto reqSignInMemberDto, String userAgent, PasswordEncoder passwordEncoder);

    Message getOauthSuiteToken(String accessToken, String userAgent, PasswordEncoder passwordEncoder);

    Message getAppleOauthSuiteToken(String accessToken, String userAgent, PasswordEncoder passwordEncoder);
    Map<String, Object> saveMemberInfo(ReqSignUpMemberDto reqSignUpMemberDto);
    void uploadImageS3(Long memberId, MultipartFile file);
    ResMemberInfoDto getMemberInfo(AuthorizerDto authorizerDto);

    void updateMemberInfo(AuthorizerDto authorizerDto, ReqUpdateMemberDto reqUpdateMemberDto, MultipartFile file);

    void withdrawalMember(AuthorizerDto authorizerDto);

    void checkEmail(EmailDto emailDto);

    String sendSms(String phoneNumber);

    Map<String, Object> lookupEmailByPhoneNumber(String phoneNumber);

    void lookupPassordByPhoneNumber(String email, String newPassword);
}
