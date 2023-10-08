package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.ResAnpOfMemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class AnpService {
    private final String ANP_POINT_URI;
    private final RestTemplate restTemplate;

    public AnpService(String ANP_POINT_URI, RestTemplate restTemplate) {
        this.ANP_POINT_URI = ANP_POINT_URI;
        this.restTemplate = restTemplate;
    }


    public int getPoint(Long memberId) {
        String url = ANP_POINT_URI + memberId;
        ResponseEntity<ResAnpOfMemberDto> anpOfMemberDto = restTemplate.getForEntity(url, ResAnpOfMemberDto.class);
        return Objects.requireNonNull(anpOfMemberDto.getBody()).getPoint();

    }
}
