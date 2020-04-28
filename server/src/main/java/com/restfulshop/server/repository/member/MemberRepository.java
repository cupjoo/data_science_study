package com.restfulshop.server.repository.member;

import com.restfulshop.server.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByOrderByCreatedDate();
}
