package com.suite.suite_user_service.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResDashBoardAvgDto {
    private int attendanceCompleteCount;
    private int missionCompleteCount;
    private Double attendanceAvgRate;
    private Double missionAvgRate;


    @Builder
    public ResDashBoardAvgDto(int attendanceCompleteCount, int missionCompleteCount, Double attendanceAvgRate, Double missionAvgRate) {
        this.attendanceCompleteCount = attendanceCompleteCount;
        this.missionCompleteCount = missionCompleteCount;
        this.attendanceAvgRate = attendanceAvgRate;
        this.missionAvgRate = missionAvgRate;
    }
}
