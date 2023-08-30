package com.suite.suite_user_service.member.controller;

import com.suite.suite_user_service.member.dto.*;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import com.suite.suite_user_service.member.service.EmailService;
import com.suite.suite_user_service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;

import java.util.Map;

import static com.suite.suite_user_service.member.security.JwtInfoExtractor.getSuiteAuthorizer;


@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/signup")
    public ResponseEntity<Message> signupSuite(@Valid @RequestBody ReqSignUpMemberDto reqSignUpMemberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        reqSignUpMemberDto.encodePassword(passwordEncoder);
        return ResponseEntity.ok(new Message(StatusCode.OK, memberService.saveMemberInfo(reqSignUpMemberDto)));
    }

    @PostMapping(value = "/profile-image/{memberId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Message> uploadProfileImage(@PathVariable Long memberId, @RequestPart(required = false) MultipartFile file) {
        memberService.uploadImageS3(memberId, file);
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @PostMapping("/auth/mail")
    public ResponseEntity<Message> sendEmailCode(@Valid @RequestBody EmailDto emailDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        EmailDto resEmailCode = emailService.sendEmailCode(emailDto);
        return ResponseEntity.ok(new Message(StatusCode.OK, resEmailCode));
    }

    @PostMapping("/verification/email")
    public ResponseEntity<Message> verifyEmail(@Valid @RequestBody EmailDto emailDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        memberService.checkEmail(emailDto);
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }


    @PostMapping("/signin")
    public ResponseEntity<Message> loginSuite(@Valid @RequestBody ReqSignInMemberDto reqSignInMemberDto, BindingResult bindingResult, @RequestHeader("User-Agent") String userAgent) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        Token token = memberService.getSuiteToken(reqSignInMemberDto, userAgent, passwordEncoder);
        return ResponseEntity.ok(new Message(StatusCode.OK, token));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Message> loginAuthSuite(@RequestBody Map<String, String> token, @RequestHeader("User-Agent") String userAgent) {
        return ResponseEntity.ok(memberService.getOauthSuiteToken(token.get("access_token"), userAgent, passwordEncoder));
    }

    @PostMapping("/id")
    public ResponseEntity<Message> findSuiteId(@RequestBody Map<String, String> inputPhoneByMember) {
        return ResponseEntity.ok(new Message(StatusCode.OK, memberService.lookupEmailByPhoneNumber(inputPhoneByMember.get("phoneNumber"))));
    }

    @PatchMapping("/pw")
    public ResponseEntity<Message> findSuitePw(@RequestBody ReqLookupPasswordDto reqLookupPasswordDto) {
        reqLookupPasswordDto.encodePassword(passwordEncoder);
        memberService.lookupPassordByPhoneNumber(reqLookupPasswordDto.getEmail(), reqLookupPasswordDto.getNewPassword());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @GetMapping("/m/profile")
    public ResponseEntity<Message> getSuiteProfile() {
        ResMemberInfoDto resMemberInfoDto = memberService.getMemberInfo(getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK, resMemberInfoDto));
    }

    @PatchMapping(value = "/m/update", consumes = {"multipart/form-data"})
    public ResponseEntity<Message> updateSuiteProfile(@Valid @RequestPart ReqUpdateMemberDto reqUpdateMemberDto, BindingResult bindingResult, @RequestPart(required = false) MultipartFile file) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        memberService.updateMemberInfo(getSuiteAuthorizer(), reqUpdateMemberDto, file);
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @PostMapping("/m/delete")
    public ResponseEntity<Message> deleteSuiteMember() {
        memberService.withdrawalMember(getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @PostMapping("/send-sms")
    public ResponseEntity<Message> sendSmsWithAuthCode(@RequestBody Map<String, String> phone) {
        String koreanPhoneNumber = "+82" + phone.get("phoneNumber").replaceAll("-", "");
        String authCode = memberService.sendSms(koreanPhoneNumber);
        return ResponseEntity.ok(new Message(StatusCode.OK, authCode));
    }

}
