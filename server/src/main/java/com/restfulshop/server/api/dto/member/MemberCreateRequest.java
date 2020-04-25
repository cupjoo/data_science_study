package com.restfulshop.server.api.dto.member;

import com.restfulshop.server.domain.member.Address;
import com.restfulshop.server.domain.member.Member;
import lombok.*;

@Getter
@NoArgsConstructor
public class MemberCreateRequest {

    private String name;
    private Address address;

    @Builder
    public MemberCreateRequest(String name, Address address){
        this.name = name;
        this.address = address;
    }

    public Member toEntity(){
        return Member.builder()
                .name(this.name).address(this.address).build();
    }
}
