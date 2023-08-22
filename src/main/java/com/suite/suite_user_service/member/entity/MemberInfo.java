package com.suite.suite_user_service.member.entity;

import com.suite.suite_user_service.member.dto.ReqSignUpMemberDto;
import com.suite.suite_user_service.member.dto.ReqUpdateMemberDto;
import com.suite.suite_user_service.member.dto.StudyCategory;
import com.suite.suite_user_service.member.dto.StudyType;
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
    @Enumerated(EnumType.STRING)
    private StudyCategory preferStudy;

    @Column(name = "study_method")
    @Enumerated(EnumType.STRING)
    private StudyType studyMethod;

    //프로필 이미지
    private String profileImage;

    @Builder
    public MemberInfo(Long memberInfoId, Member memberId, String name, String nickname, String phone, String securityNum, StudyCategory preferStudy, StudyType studyMethod, String profileImage) {
        this.memberInfoId = memberInfoId;
        this.memberId = memberId;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.securityNum = securityNum;
        this.preferStudy = preferStudy;
        this.studyMethod = studyMethod;
        this.profileImage = profileImage;
    }

    public ReqSignUpMemberDto entityToDto() {
        return ReqSignUpMemberDto.builder()
                .name(name)
                .nickName(nickname)
                .phone(phone)
                .securityNum(securityNum)
                .preferStudy(preferStudy)
                .studyMethod(studyMethod).build();
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public void updateProfile(ReqUpdateMemberDto reqUpdateMemberDto) {
        this.nickname = reqUpdateMemberDto.getNickName();
        this.phone = reqUpdateMemberDto.getPhone();
        this.preferStudy = reqUpdateMemberDto.getPreferStudy();
        this.studyMethod = reqUpdateMemberDto.getStudyMethod();
        this.profileImage = reqUpdateMemberDto.getProfileImage();
    }
}
