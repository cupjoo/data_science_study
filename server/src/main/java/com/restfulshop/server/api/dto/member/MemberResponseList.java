package com.restfulshop.server.api.dto.member;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberResponseList {

    private List<MemberResponse> data;
    private final int count;

    public MemberResponseList(List<MemberResponse> data){
        this.data = data;
        this.count = data.size();
    }
}
