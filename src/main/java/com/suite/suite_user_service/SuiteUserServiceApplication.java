package com.suite.suite_user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SuiteUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuiteUserServiceApplication.class, args);
    }

}
