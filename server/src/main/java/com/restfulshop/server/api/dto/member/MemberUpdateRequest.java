package com.restfulshop.server.api.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequest {

    private Long id;
    private String name;

    public MemberUpdateRequest(Long id, String name){
        this.id = id;
        this.name = name;
    }
}
