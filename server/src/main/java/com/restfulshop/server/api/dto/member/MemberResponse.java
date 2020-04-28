package com.restfulshop.server.api.dto.member;

import com.restfulshop.server.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberResponse {

    private Long id;
    private String name;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
    }
}
