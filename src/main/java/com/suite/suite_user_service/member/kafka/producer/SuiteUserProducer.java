package com.suite.suite_user_service.member.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteUserProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${topic.USER_REGISTRATION_FCM}")
    private String USER_REGISTRATION_FCM;
    @Value("${topic.USER_REGISTRATION_USERMETAINFO}")
    private String USER_REGISTRATION_USERMETAINFO;

    public void userRegistrationFCMProducer(Map<String, Object> data) {
        log.info("User-Registration-FCM message : {}", data);
        this.kafkaTemplate.send(USER_REGISTRATION_FCM, makeMessage(data));
    }

    public void userRegistrationMetaInfoProducer(Map<String, Object> data) {
        log.info("User-Registration-UserMetaInfo message : {}", data);
        this.kafkaTemplate.send(USER_REGISTRATION_USERMETAINFO, makeMessage(data));
    }

    public Map<String, Object> createMemberMeta(Long memberId, String fcm, String accountStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("fcm", fcm);
        map.put("accountStatus", accountStatus);
        return map;
    }

    private String makeMessage(Map<String, Object> data) {
        JSONObject obj = new JSONObject();
        obj.put("uuid", "SuiteRoomProducer/" + Instant.now().toEpochMilli());
        obj.put("data", data);
        return obj.toJSONString();
    }

}
