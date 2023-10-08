package com.suite.suite_user_service.member.auth;

import com.suite.suite_user_service.member.dto.GoogleAuthDto;
import com.suite.suite_user_service.member.dto.ReqSignInMemberDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class GoogleAuth {
    public static final String USERINFO_URL = "https://oauth2.googleapis.com/tokeninfo";

    public ReqSignInMemberDto getGoogleMemberInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> token = Map.of("id_token", accessToken);
        GoogleAuthDto googleAuthDto = restTemplate.postForEntity(USERINFO_URL, token, GoogleAuthDto.class).getBody();

        return ReqSignInMemberDto.builder()
                .email(googleAuthDto.getEmail())
                .password(googleAuthDto.getSub())
                .isOauth(true).build();
    }
}
