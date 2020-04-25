package com.restfulshop.server.api.dto.member;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemberResponseList {

    private List<MemberResponse> data = new ArrayList<>();
    private final int count;

    public MemberResponseList(List<MemberResponse> data){
        this.data = data;
        this.count = data.size();
    }
}
