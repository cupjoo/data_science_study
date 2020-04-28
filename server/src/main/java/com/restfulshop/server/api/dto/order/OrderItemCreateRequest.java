package com.restfulshop.server.api.dto.order;

import lombok.*;

@Getter
@NoArgsConstructor
public class OrderItemCreateRequest {

    private Long itemId;
    private int count;

    @Builder
    public OrderItemCreateRequest(Long itemId, int count){
        this.itemId = itemId;
        this.count = count;
    }
}
