package com.suite.suite_user_service.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResAnpOfMemberDto {
    private Long memberId;
    private Integer point;

    @Builder
    public ResAnpOfMemberDto(Long memberId, Integer point) {
        this.memberId = memberId;
        this.point = point;
    }
}
