package com.suite.suite_user_service.member.auth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.suite.suite_user_service.member.dto.ReqSignInMemberDto;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@Component
public class KakaoAuth {
    public static final String USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
    public ReqSignInMemberDto getKakaoMemberInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        try {
            URL url = new URL(USERINFO_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("result : " + result);
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            return ReqSignInMemberDto.builder()
                    .email(kakaoAccount.getAsJsonObject().get("email").getAsString())
                    .password(element.getAsJsonObject().get("id").getAsString()).build();

        } catch (MalformedURLException e) {
            throw new CustomException(StatusCode.MALFORMED);
        } catch (IOException e) {
            throw new CustomException(StatusCode.FAILED_REQUEST);
        }
    }
}
