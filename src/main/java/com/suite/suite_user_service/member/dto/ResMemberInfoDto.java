package com.suite.suite_user_service.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResMemberInfoDto {
    private Long memberId;
    private String email;
    private String name;
    private String nickName;
    private String phone;
    private String securityNum;
    private StudyCategory preferStudy;
    private StudyType studyMethod;
    private String accountStatus;

    @Builder
    public ResMemberInfoDto(Long memberId, String email, String name, String nickName, String phone, String securityNum, StudyCategory preferStudy, StudyType studyMethod, String accountStatus) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.phone = phone;
        this.securityNum = securityNum;
        this.preferStudy = preferStudy;
        this.studyMethod = studyMethod;
        this.accountStatus = accountStatus;
    }
}
