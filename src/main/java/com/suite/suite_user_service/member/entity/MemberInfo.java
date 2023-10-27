package com.suite.suite_user_service.member.entity;

import com.suite.suite_user_service.baseTime.BaseTimeEntity;
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
public class MemberInfo extends BaseTimeEntity {

    @Id
    @Column(name = "member_info_id")
    private Long memberInfoId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    private String name;

    private String nickname;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "prefer_study")
    @Enumerated(EnumType.STRING)
    private StudyCategory preferStudy;

    @Column(name = "study_method")
    @Enumerated(EnumType.STRING)
    private StudyType studyMethod;

    //프로필 이미지
    private String profileImage;

    @Column(name = "account_number")
    private String accountNumber;

    @Builder
    public MemberInfo(Long memberInfoId, Member member, String name, String nickname, String phone, StudyCategory preferStudy, StudyType studyMethod, String profileImage, String accountNumber) {
        this.memberInfoId = memberInfoId;
        this.member = member;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.preferStudy = preferStudy;
        this.studyMethod = studyMethod;
        this.profileImage = profileImage;
        this.accountNumber = accountNumber;
    }

    public ReqSignUpMemberDto entityToDto() {
        return ReqSignUpMemberDto.builder()
                .name(name)
                .nickName(nickname)
                .phone(phone)
                .preferStudy(preferStudy)
                .studyMethod(studyMethod).build();
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setMember(Member memberId) {
        this.member = memberId;
    }

    public void updateProfile(ReqUpdateMemberDto reqUpdateMemberDto) {
        this.nickname = reqUpdateMemberDto.getNickName();
        this.phone = reqUpdateMemberDto.getPhone();
        this.preferStudy = reqUpdateMemberDto.getPreferStudy();
        this.studyMethod = reqUpdateMemberDto.getStudyMethod();
    }

}
