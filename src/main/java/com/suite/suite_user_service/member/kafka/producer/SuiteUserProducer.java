package com.suite.suite_user_service.member.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteUserProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, Object data) {
        log.info("User-Registration-FCM message : {}", data);
        JSONObject obj = new JSONObject();
        obj.put("uuid", "UserRegistrationProducer/" + Instant.now().toEpochMilli());
        obj.put("data", data);
        this.kafkaTemplate.send(topic, obj.toJSONString());
    }

}
