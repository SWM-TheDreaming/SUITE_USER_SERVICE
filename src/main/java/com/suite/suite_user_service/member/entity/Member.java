package com.suite.suite_user_service.member.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.suite.suite_user_service.member.dto.AccountStatus;
import com.suite.suite_user_service.member.dto.ResMemberInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member {

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

    @Column(name = "is_auth")
    private boolean isAuth;

    @OneToOne(mappedBy = "memberId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private MemberInfo memberInfo;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Mark> markList = new ArrayList<>();

    @Builder
    public Member(String email, String password, String role, String accountStatus, boolean isAuth) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
        this.isAuth = isAuth;
    }

    public ResMemberInfoDto toResMemberInfoDto() {
        return ResMemberInfoDto.builder()
                .memberId(memberId)
                .email(email)
                .name(memberInfo.getName())
                .nickName(memberInfo.getNickname())
                .phone(memberInfo.getPhone())
                .securityNum(memberInfo.getSecurityNum())
                .preferStudy(memberInfo.getPreferStudy())
                .studyMethod(memberInfo.getStudyMethod())
                .accountStatus(accountStatus)
                .isAuth(isAuth).build();
    }

    public void addMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
        memberInfo.setMemberId(this);
    }

    public void updateAccountStatus() {
        this.accountStatus = String.valueOf(AccountStatus.DISABLED);
    }

}