package com.suite.suite_user_service.member.controller;

import com.suite.suite_user_service.member.dto.*;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import com.suite.suite_user_service.member.service.EmailService;
import com.suite.suite_user_service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/signup")
    public ResponseEntity<Message> signupSuite(@Valid @RequestBody ReqSignUpMemberDto reqSignUpMemberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        reqSignUpMemberDto.encodePassword(passwordEncoder);
        memberService.saveMemberInfo(reqSignUpMemberDto);
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @PostMapping("/auth/mail")
    public ResponseEntity<Message> verifyEmail(@Valid @RequestBody EmailDto emailDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        EmailDto resEmailCode = emailService.sendEmailCode(emailDto);
        return ResponseEntity.ok(new Message(StatusCode.OK, resEmailCode));
    }

    @PostMapping("/signin")
    public ResponseEntity<Message> loginSuite(@Valid @RequestBody ReqSignInMemberDto reqSignInMemberDto, BindingResult bindingResult, @RequestHeader("User-Agent") String userAgent) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        Token token = memberService.getSuiteToken(reqSignInMemberDto, userAgent);
        return ResponseEntity.ok(new Message(StatusCode.OK, token));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Message> loginAuthSuite(@RequestBody Map<String, String> token, @RequestHeader("User-Agent") String userAgent) {
        return ResponseEntity.ok(memberService.getAuthSuiteToken(token.get("access_token"), userAgent));
    }

    @PostMapping("/id")
    public ResponseEntity<Message> findSuiteId() {
        return null;
    }

    @PostMapping("/pw")
    public ResponseEntity<Message> findSuitePw() {
        return null;
    }

    @GetMapping("/m/profile")
    public ResponseEntity<Message> getSuiteProfile() {
        ResMemberInfoDto resMemberInfoDto = memberService.getMemberInfo(getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK, resMemberInfoDto));
    }

    @PatchMapping("/m/update")
    public ResponseEntity<Message> updateSuiteProfile(@Valid @RequestBody ReqUpdateMemberDto reqUpdateMemberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        memberService.updateMemberInfo(getSuiteAuthorizer(), reqUpdateMemberDto);
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @PostMapping("/m/delete")
    public ResponseEntity<Message> deleteSuiteMember() {
        memberService.withdrawalMember(getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

}
