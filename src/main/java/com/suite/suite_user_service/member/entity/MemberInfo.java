package com.suite.suite_user_service.member.entity;

import com.suite.suite_user_service.member.dto.ReqSignUpMemberDto;
import com.suite.suite_user_service.member.dto.ReqUpdateMemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member_info")
public class MemberInfo {

    @Id
    @Column(name = "member_info_id")
    private Long memberInfoId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Member memberId;

    private String name;

    private String nickname;

    private String phone;

    @Column(name = "security_num")
    private String securityNum;

    @Column(name = "prefer_study")
    private String preferStudy;

    @Column(name = "location")
    private String location;

    @Column(name = "study_method")
    private String studyMethod;


    //프로필 이미지
    //private ProfileImage profileImage;

    @Builder
    public MemberInfo(Long memberInfoId, Member memberId, String name, String nickname, String phone, String securityNum, String preferStudy, String location, String studyMethod) {
        this.memberInfoId = memberInfoId;
        this.memberId = memberId;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.securityNum = securityNum;
        this.preferStudy = preferStudy;
        this.location = location;
        this.studyMethod = studyMethod;
    }

    public ReqSignUpMemberDto entityToDto() {
        return ReqSignUpMemberDto.builder()
                .name(name)
                .nickName(nickname)
                .phone(phone)
                .securityNum(securityNum)
                .preferStudy(preferStudy)
                .location(location)
                .studyMethod(studyMethod).build();
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public void updateProfile(ReqUpdateMemberDto reqUpdateMemberDto) {
        this.nickname = reqUpdateMemberDto.getNickName();
        this.phone = reqUpdateMemberDto.getPhone();
        this.preferStudy = reqUpdateMemberDto.getPreferStudy();
        this.location = reqUpdateMemberDto.getLocation();
        this.studyMethod = reqUpdateMemberDto.getStudyMethod();
    }
}
