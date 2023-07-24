package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.*;
import com.suite.suite_user_service.member.entity.Member;
import com.suite.suite_user_service.member.entity.MemberInfo;
import com.suite.suite_user_service.member.entity.RefreshToken;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import com.suite.suite_user_service.member.repository.MemberInfoRepository;
import com.suite.suite_user_service.member.repository.MemberRepository;
import com.suite.suite_user_service.member.repository.RefreshTokenRepository;
import com.suite.suite_user_service.member.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public Message getSuiteToken(ReqSignInMemberDto reqSignInMemberDto, String userAgent) {
        Member member = memberRepository.findByEmail(reqSignInMemberDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.USERNAME_OR_PASSWORD_NOT_FOUND));

        if(member.getAccountStatus().equals(AccountStatus.DORMANT.getStatus()))
            throw new CustomException(StatusCode.DORMANT_ACCOUNT);
        else if(member.getAccountStatus().equals(AccountStatus.DISABLED.getStatus()))
            throw new CustomException(StatusCode.DISABLED_ACCOUNT);

        Token token = jwtTokenProvider.createToken(member);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .keyId(token.getKey())
                        .refreshToken(token.getRefreshToken())
                        .userAgent(userAgent).build());

        return new Message(StatusCode.OK, token);
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

    private Boolean isActiveMember(Member member) {
        if (member.getAccountStatus() == String.valueOf(AccountStatus.ACTIVATE)){
            return true;
        }

        return false;
    }



}
