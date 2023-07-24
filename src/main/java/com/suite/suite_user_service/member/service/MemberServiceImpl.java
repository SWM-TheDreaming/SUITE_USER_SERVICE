package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.AuthorizerDto;
import com.suite.suite_user_service.member.dto.Message;
import com.suite.suite_user_service.member.dto.ReqSignUpMemberDto;
import com.suite.suite_user_service.member.dto.ReqUpdateMemberDto;
import com.suite.suite_user_service.member.entity.Member;
import com.suite.suite_user_service.member.entity.MemberInfo;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import com.suite.suite_user_service.member.repository.MemberInfoRepository;
import com.suite.suite_user_service.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Message saveMemberInfo(ReqSignUpMemberDto reqSignUpMemberDto) {
        memberInfoRepository.findByMemberId_Email(reqSignUpMemberDto.getEmail()).ifPresent(
                memberInfo -> { throw new CustomException(StatusCode.REGISTERED_EMAIL); });

        Member member = reqSignUpMemberDto.toMemberEntity();
        MemberInfo memberInfo = reqSignUpMemberDto.toMemberInfoEntity();
        member.addMemberInfo(memberInfo);
        memberRepository.save(member);
        memberInfoRepository.save(memberInfo);

        return new Message(StatusCode.OK);
    }

    @Override
    public Message getMemberInfo(AuthorizerDto authorizerDto) {
        Member member = memberRepository.findByEmail(authorizerDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));

        return new Message(StatusCode.OK, member.entityToDto());
    }


    @Override
    @Transactional
    public Message updateMemberInfo(AuthorizerDto authorizerDto, ReqUpdateMemberDto reqUpdateMemberDto) {
        MemberInfo memberInfo = memberInfoRepository.findByMemberId_Email(authorizerDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));

        memberInfo.updateProfile(reqUpdateMemberDto);
        return new Message(StatusCode.OK);
    }

    @Override
    public Message withdrawalMember(AuthorizerDto authorizerDto) {
        Member member = memberRepository.findByEmail(authorizerDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
        member.updateAccountStatus();

        return new Message(StatusCode.OK);
    }
}
