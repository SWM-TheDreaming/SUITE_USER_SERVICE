package com.suite.suite_user_service.member.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteUserProducer {
    @Value("${topic.USER_REGISTRATION_FCM}")
    private String USER_REGISTRATION_FCM;
    @Value("${topic.USER_REGISTRATION_USERMETAINFO}")
    private String USER_REGISTRATION_USERMETAINFO;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendUserRegistrationFCMMessage(String message) {
        log.info("User-Registration-FCM message : {}", message);
        this.kafkaTemplate.send(USER_REGISTRATION_FCM, message);
    }

    public void sendUserRegistrationUserMetaInfoMessage(String message) {
        log.info("User-Registration-UserMetaInfo message : {}", message);
        this.kafkaTemplate.send(USER_REGISTRATION_USERMETAINFO, message);
    }

}
