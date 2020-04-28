package com.restfulshop.server.repository.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restfulshop.server.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.restfulshop.server.domain.member.QMember.member;
import static com.restfulshop.server.domain.order.QDelivery.delivery;
import static com.restfulshop.server.domain.order.QOrder.order;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Order> findAllWithFetch(){
        return queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .fetchResults()
                .getResults();
    }
}
