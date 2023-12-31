package com.suite.suite_user_service.member.entity;

import com.suite.suite_user_service.baseTime.BaseTimeEntity;
import com.suite.suite_user_service.member.dto.AccountStatus;
import com.suite.suite_user_service.member.dto.ResDashBoardAvgDto;
import com.suite.suite_user_service.member.dto.ResMemberInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "account_status")
    private String accountStatus;

    @Column(name = "is_oauth")
    private boolean isOauth;

    @OneToOne(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private MemberInfo memberInfo;


    @Builder
    public Member(String email, String password, String role, String accountStatus, boolean isOauth) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
        this.isOauth = isOauth;
    }

    public ResMemberInfoDto toResMemberInfoDto(ResDashBoardAvgDto resDashBoardAvgDto) {
        return ResMemberInfoDto.builder()
                .memberId(memberId)
                .email(email)
                .name(memberInfo.getName())
                .nickName(memberInfo.getNickname())
                .phone(memberInfo.getPhone())
                .preferStudy(memberInfo.getPreferStudy())
                .studyMethod(memberInfo.getStudyMethod())
                .accountStatus(accountStatus)
                .isOauth(isOauth)
                .accountNumber(memberInfo.getAccountNumber())
                .resDashBoardAvgDto(resDashBoardAvgDto).build();
    }

    public void addMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
        memberInfo.setMember(this);
    }

    public void updateAccountStatus() {
        this.accountStatus = String.valueOf(AccountStatus.DISABLED);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}