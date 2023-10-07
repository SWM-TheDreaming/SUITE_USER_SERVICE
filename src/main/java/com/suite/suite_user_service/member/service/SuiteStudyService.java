package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.ResDashBoardAvgDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class SuiteStudyService {

    private final String STUDY_AVG_URI;

    private final RestTemplate restTemplate;

    public SuiteStudyService(String STUDY_AVG_URI, RestTemplate restTemplate) {
        this.STUDY_AVG_URI = STUDY_AVG_URI;
        this.restTemplate = restTemplate;
    }

    public ResDashBoardAvgDto getStudyAvgInfo(Long memberId) {
        String url = STUDY_AVG_URI + memberId;
        ResponseEntity<ResDashBoardAvgDto> studyAvgInfoDto = restTemplate.getForEntity(url, ResDashBoardAvgDto.class);
        return Objects.requireNonNull(studyAvgInfoDto.getBody());
    }
}
