package com.suite.suite_user_service.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class ReqUpdateMemberDto {
    @Pattern(regexp = "^.{1,8}$")
    private String nickName;
    @Pattern(regexp = "^01[0-9]{1}-[0-9]{4}-[0-9]{4}$")
    private String phone;

    private StudyCategory preferStudy;
    private StudyType studyMethod;
}
