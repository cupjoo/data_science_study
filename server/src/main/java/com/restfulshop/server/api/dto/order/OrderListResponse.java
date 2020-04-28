package com.restfulshop.server.api.dto.order;

import com.restfulshop.server.api.dto.order.OrderResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderListResponse {

    private List<OrderResponse> data;
    private final int count;

    public OrderListResponse(List<OrderResponse> data){
        this.data = data;
        this.count = data.size();
    }
}