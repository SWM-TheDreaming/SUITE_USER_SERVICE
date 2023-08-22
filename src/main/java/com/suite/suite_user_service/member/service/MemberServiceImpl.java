package com.suite.suite_user_service.member.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.suite.suite_user_service.member.auth.KakaoAuth;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    public static final String FILENAME = "SUITE_PROFILE_";
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtCreator jwtCreator;
    private final KakaoAuth kakaoAuth;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @Override
    public Token getSuiteToken(ReqSignInMemberDto reqSignInMemberDto, String userAgent, PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findByEmail(reqSignInMemberDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.USERNAME_NOT_FOUND));

        if(!passwordEncoder.matches(reqSignInMemberDto.getPassword(), member.getPassword()))
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        else if(member.getAccountStatus().equals(AccountStatus.DORMANT.getStatus()))
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

    @Override
    public Message getOauthSuiteToken( String accessToken, String userAgent, PasswordEncoder passwordEncoder) {
        ReqSignInMemberDto reqSignInMemberDto = kakaoAuth.getKakaoMemberInfo(accessToken);

        Optional<Token> token = memberRepository.findByEmail(reqSignInMemberDto.getEmail()).map(member -> verifyOauthAccount(reqSignInMemberDto, userAgent, passwordEncoder));
        return token.map(suiteToken -> new Message(StatusCode.OK, suiteToken)).orElseGet(() -> new Message(StatusCode.CREATED, reqSignInMemberDto));
    }

    @Override
    @Transactional
    public void saveMemberInfo(ReqSignUpMemberDto reqSignUpMemberDto, MultipartFile file) {
        memberInfoRepository.findByMemberId_Email(reqSignUpMemberDto.getEmail()).ifPresent(
                memberInfo -> { throw new CustomException(StatusCode.REGISTERED_EMAIL); });

        Member member = reqSignUpMemberDto.toMemberEntity();
        MemberInfo memberInfo = reqSignUpMemberDto.toMemberInfoEntity();
        member.addMemberInfo(memberInfo);
        memberRepository.save(member);
        memberInfo.setProfileImage(saveProfileImage(member.getMemberId(), file));
        memberInfoRepository.save(memberInfo);
    }

    @Override
    public ResMemberInfoDto getMemberInfo(AuthorizerDto authorizerDto) {
        Member member = memberRepository.findByEmail(authorizerDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));

        return member.toResMemberInfoDto(getFileURL(member.getMemberInfo().getProfileImage()));
    }

    @Override
    @Transactional
    public void updateMemberInfo(AuthorizerDto authorizerDto, ReqUpdateMemberDto reqUpdateMemberDto, MultipartFile file) {
        MemberInfo memberInfo = memberInfoRepository.findByMemberId_Email(authorizerDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
        memberInfo.setProfileImage(saveProfileImage(memberInfo.getMemberId().getMemberId(), file));
        memberInfo.updateProfile(reqUpdateMemberDto);
    }

    @Override
    public void withdrawalMember(AuthorizerDto authorizerDto) {
        Member member = memberRepository.findByEmail(authorizerDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
        member.updateAccountStatus();
    }

    @Override
    public void checkEmail(EmailDto emailDto) {
        memberRepository.findByEmail(emailDto.getEmail()).ifPresent(
                e -> {throw new CustomException(StatusCode.REGISTERED_EMAIL);});
    }

    private Token verifyOauthAccount(ReqSignInMemberDto reqSignInMemberDto, String userAgent, PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findByEmail(reqSignInMemberDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.USERNAME_NOT_FOUND));

        if(!passwordEncoder.matches(reqSignInMemberDto.getPassword(), member.getPassword()))
            throw new CustomException(StatusCode.REGISTERED_EMAIL);

        Token token = jwtCreator.createToken(member);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .keyId(token.getKey())
                        .refreshToken(token.getRefreshToken())
                        .userAgent(userAgent).build());
        return token;
    }

    private String saveProfileImage(Long memberId, MultipartFile multiPartFile) {
        try {
            String fileName = parseUUID(memberId, Objects.requireNonNull(multiPartFile.getOriginalFilename()));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multiPartFile.getSize());
            metadata.setContentType("image/png");
            metadata.setContentDisposition("inline");
            amazonS3.putObject(bucket, fileName, multiPartFile.getInputStream(), metadata);
            return fileName;
        } catch (IOException e) {
            throw new CustomException(StatusCode.FAILED_REQUEST);
        }

    }

    private String getFileURL(String fileName) {
        Date expiration = new Date();
        long expTomeMillis = expiration.getTime();
        expTomeMillis += 1000 * 60 * 60; //1hour
        expiration.setTime(expTomeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, (fileName).replace(File.separatorChar, '/'))
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    private String parseUUID(Long memberId, String fileName) {
        Date now = new Date();
        StringBuffer sb = new StringBuffer(FILENAME);

        String extension = fileName.substring(fileName.lastIndexOf("."));
        sb.append(memberId);
        sb.append(extension);

        return sb.toString();
    }
}
