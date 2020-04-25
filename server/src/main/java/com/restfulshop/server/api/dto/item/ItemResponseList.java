package com.restfulshop.server.api.dto.item;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemResponseList {

    private List<ItemResponse> data;
    private final int count;

    public ItemResponseList(List<ItemResponse> data){
        this.data = data;
        this.count = data.size();
    }
}