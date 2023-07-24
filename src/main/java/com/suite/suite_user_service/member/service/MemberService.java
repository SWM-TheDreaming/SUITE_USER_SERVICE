package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.*;
import com.suite.suite_user_service.member.entity.Member;

public interface MemberService {

    Message getSuiteToken(ReqSignInMemberDto reqSignInMemberDto, String userAgent);
    Message saveMemberInfo(ReqSignUpMemberDto reqSignUpMemberDto);
    Message getMemberInfo(AuthorizerDto authorizerDto);

    Message updateMemberInfo(AuthorizerDto authorizerDto, ReqUpdateMemberDto reqUpdateMemberDto);

    Message withdrawalMember(AuthorizerDto authorizerDto);

}
