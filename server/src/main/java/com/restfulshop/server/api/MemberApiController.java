package com.restfulshop.server.api;

import com.restfulshop.server.api.dto.member.*;
import com.restfulshop.server.service.MemberService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping
    public Long create(@RequestBody @Valid MemberCreateRequest request){
        return memberService.create(request);
    }

    @GetMapping
    public MemberResponseList findAll(){
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    public MemberResponse findById(@PathVariable("id") Long id){
        return memberService.findById(id);
    }

    @PutMapping("/{id}")
    public MemberResponse update(
            @PathVariable("id") Long id,
            @RequestBody @Valid MemberUpdateRequest request){

        memberService.update(id, request.getName());  // 필요한 인자만 전달
        return memberService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable("id") Long id){
        memberService.delete(id);
    }
}
