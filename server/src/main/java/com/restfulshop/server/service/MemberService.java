package com.restfulshop.server.service;

import com.restfulshop.server.domain.member.Member;
import com.restfulshop.server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long create(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member "+id+" does not exist."));
    }

    public List<Member> findAll(){
        return memberRepository.findAllByOrderByCreatedDate();
    }

    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member "+id+" does not exist."));
        member.changeName(name);
    }

    @Transactional
    public void delete(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member "+id+" does not exist."));
        memberRepository.delete(member);
    }
}
