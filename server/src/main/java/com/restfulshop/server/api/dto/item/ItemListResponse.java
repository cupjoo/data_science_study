package com.restfulshop.server.api.dto.item;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemListResponse {

    private List<ItemResponse> data;
    private final int count;

    public ItemListResponse(List<ItemResponse> data){
        this.data = data;
        this.count = data.size();
    }
}