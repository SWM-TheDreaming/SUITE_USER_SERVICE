package com.suite.suite_user_service.member.repository;

import com.suite.suite_user_service.member.entity.Member;
import com.suite.suite_user_service.member.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    Optional<MemberInfo> findByMember_Email(String email);
    Optional<MemberInfo> findByMember_MemberId(Long memberId);
}
