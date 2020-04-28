package com.restfulshop.server.repository.order;

import com.restfulshop.server.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>{
}
