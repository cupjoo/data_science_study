package com.restfulshop.server.domain.order;

import java.util.List;

public interface OrderQueryRepository {

    List<Order> findAllWithFetch();
}
