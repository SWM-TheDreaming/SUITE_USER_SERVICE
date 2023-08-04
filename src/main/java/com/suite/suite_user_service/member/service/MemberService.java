package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.*;
import com.suite.suite_user_service.member.security.dto.AuthorizerDto;

public interface MemberService {

    Token getSuiteToken(ReqSignInMemberDto reqSignInMemberDto, String userAgent);

    Message getAuthSuiteToken(String accessToken, String userAgent);
    void saveMemberInfo(ReqSignUpMemberDto reqSignUpMemberDto);
    ResMemberInfoDto getMemberInfo(AuthorizerDto authorizerDto);

    void updateMemberInfo(AuthorizerDto authorizerDto, ReqUpdateMemberDto reqUpdateMemberDto);

    void withdrawalMember(AuthorizerDto authorizerDto);

    public void checkEmail(EmailDto emailDto);

}
