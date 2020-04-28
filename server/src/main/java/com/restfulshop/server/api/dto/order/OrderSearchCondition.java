package com.restfulshop.server.api.dto.order;

import com.restfulshop.server.domain.order.OrderStatus;
import com.restfulshop.server.repository.order.OrderQueryRepository;
import lombok.*;

@Getter
@NoArgsConstructor
@Setter
public class OrderSearchCondition {

    private String memberName;
    private OrderStatus orderStatus;

    @Builder
    public OrderSearchCondition(String memberName, OrderStatus orderStatus){
        this.memberName = memberName;
        this.orderStatus = orderStatus;
    }
}
