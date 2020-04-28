package com.restfulshop.server.api.dto.order;

import com.restfulshop.server.domain.order.Order;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {

    private Long id;

    public OrderResponse(Order order){
        this.id = order.getId();
    }
}
