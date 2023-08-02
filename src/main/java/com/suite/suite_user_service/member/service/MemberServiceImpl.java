package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.auth.KakaoAuth;
import com.suite.suite_user_service.member.controller.MemberController;
import com.suite.suite_user_service.member.dto.*;
import com.suite.suite_user_service.member.entity.Member;
import com.suite.suite_user_service.member.entity.MemberInfo;
import com.suite.suite_user_service.member.entity.RefreshToken;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import com.suite.suite_user_service.member.repository.MemberInfoRepository;
import com.suite.suite_user_service.member.repository.MemberRepository;
import com.suite.suite_user_service.member.repository.RefreshTokenRepository;
import com.suite.suite_user_service.member.security.JwtCreator;
import com.suite.suite_user_service.member.security.dto.AuthorizerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtCreator jwtCreator;
    private final KakaoAuth kakaoAuth;


    @Override
    public Token getSuiteToken(ReqSignInMemberDto reqSignInMemberDto, String userAgent) {
        Member member = memberRepository.findByEmail(reqSignInMemberDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.USERNAME_OR_PASSWORD_NOT_FOUND));

        if(member.getAccountStatus().equals(AccountStatus.DORMANT.getStatus()))
            throw new CustomException(StatusCode.DORMANT_ACCOUNT);
        else if(member.getAccountStatus().equals(AccountStatus.DISABLED.getStatus()))
            throw new CustomException(StatusCode.DISABLED_ACCOUNT);

        Token token = jwtCreator.createToken(member);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .keyId(token.getKey())
                        .refreshToken(token.getRefreshToken())
                        .userAgent(userAgent).build());

        return token;
    }

    public Message getAuthSuiteToken(String accessToken, String userAgent) {
        ReqSignInMemberDto reqSignInMemberDto = kakaoAuth.getKakaoMemberInfo(accessToken);

        Optional<Token> token = memberRepository.findByEmail(reqSignInMemberDto.getEmail()).map(member -> {
            return getSuiteToken(reqSignInMemberDto, userAgent);
        });
        return token.map(suiteToken -> new Message(StatusCode.OK, suiteToken)).orElseGet(() -> new Message(StatusCode.OK, reqSignInMemberDto));
    }

    @Override
    public void saveMemberInfo(ReqSignUpMemberDto reqSignUpMemberDto) {
        memberInfoRepository.findByMemberId_Email(reqSignUpMemberDto.getEmail()).ifPresent(
                memberInfo -> { throw new CustomException(StatusCode.REGISTERED_EMAIL); });

        Member member = reqSignUpMemberDto.toMemberEntity();
        MemberInfo memberInfo = reqSignUpMemberDto.toMemberInfoEntity();
        member.addMemberInfo(memberInfo);
        memberRepository.save(member);
        memberInfoRepository.save(memberInfo);
    }

    @Override
    public ResMemberInfoDto getMemberInfo(AuthorizerDto authorizerDto) {
        Member member = memberRepository.findByEmail(authorizerDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
        return member.toResMemberInfoDto();
    }


    @Override
    @Transactional
    public void updateMemberInfo(AuthorizerDto authorizerDto, ReqUpdateMemberDto reqUpdateMemberDto) {
        MemberInfo memberInfo = memberInfoRepository.findByMemberId_Email(authorizerDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
        memberInfo.updateProfile(reqUpdateMemberDto);
    }

    @Override
    public void withdrawalMember(AuthorizerDto authorizerDto) {
        Member member = memberRepository.findByEmail(authorizerDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
        member.updateAccountStatus();
    }
}
