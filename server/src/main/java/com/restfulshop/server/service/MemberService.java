package com.restfulshop.server.service;

import com.restfulshop.server.api.dto.member.MemberResponse;
import com.restfulshop.server.api.dto.member.MemberListResponse;
import com.restfulshop.server.domain.member.Member;
import com.restfulshop.server.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

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

    public MemberResponse findById(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member "+id+" does not exist."));
        return new MemberResponse(member);  // Entity to Dto
    }

    public MemberListResponse findAll(){
        return new MemberListResponse(memberRepository.findAllByOrderByCreatedDate().stream()
                .map(MemberResponse::new)  // Entity to Dto
                .collect(toList()));
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
