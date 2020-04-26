package com.restfulshop.server.api.dto.member;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberListResponse {

    private List<MemberResponse> data;
    private final int count;

    public MemberListResponse(List<MemberResponse> data){
        this.data = data;
        this.count = data.size();
    }
}
