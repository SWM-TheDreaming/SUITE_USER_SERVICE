package com.suite.suite_user_service.member.entity;

import com.suite.suite_user_service.member.dto.AccountStatus;
import com.suite.suite_user_service.member.dto.ResMemberInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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

    @OneToOne(mappedBy = "memberId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private MemberInfo memberInfo;

    @Builder
    public Member(String email, String password, String role, String accountStatus) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
    }

    public ResMemberInfoDto entityToDto() {
        return ResMemberInfoDto.builder()
                .memberId(memberId)
                .email(email)
                .name(memberInfo.getName())
                .nickName(memberInfo.getNickname())
                .phone(memberInfo.getPhone())
                .securityNum(memberInfo.getSecurityNum())
                .preferStudy(memberInfo.getPreferStudy())
                .location(memberInfo.getLocation())
                .studyMethod(memberInfo.getStudyMethod())
                .accountStatus(accountStatus).build();
    }

    public void addMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
        memberInfo.setMemberId(this);
    }

    public void updateAccountStatus() {
        this.accountStatus = String.valueOf(AccountStatus.DISABLED);
    }

}