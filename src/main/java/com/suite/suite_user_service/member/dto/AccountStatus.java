package com.suite.suite_user_service.member.dto;

import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountStatus {
    DORMANT("DORMANT"),
    ACTIVATE("ACTIVATE"),
    DISABLED("DISABLED");
    private final String status;

    public static String from(String status) {
        try {
            return AccountStatus.valueOf(status.toUpperCase()).status;
        } catch (IllegalArgumentException e) {
            throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        }
    }
}
