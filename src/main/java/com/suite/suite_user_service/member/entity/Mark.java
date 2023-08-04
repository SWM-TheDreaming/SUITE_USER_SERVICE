package com.suite.suite_user_service.member.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mark_id", nullable = false)
    private Long markId;


    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member memberId;

    @Column(name = "suite_room_id")
    private Long suiteRoomId;


    @Builder
    public Mark(Long markId, Member memberId, Long suiteRoomId) {
        this.markId = markId;
        this.memberId = memberId;
        this.suiteRoomId = suiteRoomId;
    }
}