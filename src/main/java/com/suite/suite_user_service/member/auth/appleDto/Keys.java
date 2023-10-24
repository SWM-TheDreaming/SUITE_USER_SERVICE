package com.suite.suite_user_service.member.auth.appleDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Keys {
    private List<KeyInfo> keys;
}
