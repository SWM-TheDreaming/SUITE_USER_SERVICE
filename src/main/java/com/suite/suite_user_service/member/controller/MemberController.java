package com.suite.suite_user_service.member.controller;

import com.suite.suite_user_service.member.dto.*;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import com.suite.suite_user_service.member.service.EmailService;
import com.suite.suite_user_service.member.service.JwtService;
import com.suite.suite_user_service.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import static com.suite.suite_user_service.member.security.config.SecurityUtil.getSuiteAuthorizer;


@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<Message> signupSuite(@Valid @RequestBody ReqSignUpMemberDto reqSignUpMemberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        reqSignUpMemberDto.encodePassword(passwordEncoder);
        return ResponseEntity.ok(memberService.saveMemberInfo(reqSignUpMemberDto));
    }

    @PostMapping("/auth/mail")
    public ResponseEntity<Message> verifyEmail(@Valid @RequestBody EmailDto emailDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        return ResponseEntity.ok(emailService.sendEmailCode(emailDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<Message> loginSuite(@Valid @RequestBody ReqSignInMemberDto reqSignInMemberDto, BindingResult bindingResult, @RequestHeader("User-Agent") String userAgent) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        return ResponseEntity.ok(jwtService.login(reqSignInMemberDto, userAgent));
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
        return ResponseEntity.ok(memberService.getMemberInfo(getSuiteAuthorizer()));
    }

    @PatchMapping("/m/update")
    public ResponseEntity<Message> updateSuiteProfile(@Valid @RequestBody ReqUpdateMemberDto reqUpdateMemberDto) {
        return ResponseEntity.ok(memberService.updateMemberInfo(getSuiteAuthorizer(), reqUpdateMemberDto));
    }

    @PostMapping("/m/delete")
    public ResponseEntity<Message> deleteSuiteMember() {
        return ResponseEntity.ok(memberService.withdrawalMember(getSuiteAuthorizer()));
    }

}
