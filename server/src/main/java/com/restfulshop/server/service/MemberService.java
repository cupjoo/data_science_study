package com.restfulshop.server.service;

import com.restfulshop.server.api.dto.member.MemberCreateRequest;
import com.restfulshop.server.api.dto.member.MemberResponse;
import com.restfulshop.server.api.dto.member.MemberResponseList;
import com.restfulshop.server.domain.member.Member;
import com.restfulshop.server.domain.member.MemberRepository;
import com.restfulshop.server.exception.MemberDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long create(MemberCreateRequest request){
        Member member = memberRepository.save(request.toEntity());
        return member.getId();
    }

    public MemberResponse findById(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberDoesNotExistException(id));
        return new MemberResponse(member);  // Entity to Dto
    }

    public MemberResponseList findAll(){
        return new MemberResponseList(memberRepository.findAllByOrderByCreatedDate().stream()
                .map(MemberResponse::new)  // Entity to Dto
                .collect(toList()));
    }

    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberDoesNotExistException(id));
        member.changeName(name);
    }

    @Transactional
    public void delete(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberDoesNotExistException(id));
        memberRepository.delete(member);
    }
}
