package com.suite.suite_user_service.member.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponseEntity {
    private int statusCode;
    private String message;
    private HttpStatus status;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(StatusCode e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponseEntity.builder()
                        .status(e.getStatus())
                        .statusCode(e.getStatusCode())
                        .message(e.getMessage())
                        .build());
    }
}