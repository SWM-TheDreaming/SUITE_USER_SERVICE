package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.AuthorizerDto;
import com.suite.suite_user_service.member.dto.Message;
import com.suite.suite_user_service.member.dto.ReqSignUpMemberDto;
import com.suite.suite_user_service.member.dto.ReqUpdateMemberDto;

public interface MemberService {

    Message saveMemberInfo(ReqSignUpMemberDto reqSignUpMemberDto);
    Message getMemberInfo(AuthorizerDto authorizerDto);

    Message updateMemberInfo(AuthorizerDto authorizerDto, ReqUpdateMemberDto reqUpdateMemberDto);

    Message withdrawalMember(AuthorizerDto authorizerDto);
}
