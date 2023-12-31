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
    private boolean isOauth;
    private String profileURL;
    private String accountNumber;
    private int attendanceCompleteCount;
    private int missionCompleteCount;
    private Double attendanceAvgRate;
    private Double missionAvgRate;
    private Integer point;

    @Builder
    public ResMemberInfoDto(Long memberId, String email, String name, String nickName, String phone, String securityNum, StudyCategory preferStudy, StudyType studyMethod, String accountStatus, boolean isOauth, String profileURL, String accountNumber, ResDashBoardAvgDto resDashBoardAvgDto, Integer point) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.phone = phone;
        this.securityNum = securityNum;
        this.preferStudy = preferStudy;
        this.studyMethod = studyMethod;
        this.accountStatus = accountStatus;
        this.isOauth = isOauth;
        this.profileURL = profileURL;
        this.accountNumber = accountNumber;
        this.attendanceCompleteCount = resDashBoardAvgDto.getAttendanceCompleteCount();
        this.missionCompleteCount = resDashBoardAvgDto.getMissionCompleteCount();
        this.attendanceAvgRate = resDashBoardAvgDto.getAttendanceAvgRate();
        this.missionAvgRate = resDashBoardAvgDto.getMissionAvgRate();
        this.point = point;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
