package com.restfulshop.server.domain.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.restfulshop.server.domain.member.QMember.member;
import static com.restfulshop.server.domain.order.QDelivery.delivery;
import static com.restfulshop.server.domain.order.QOrder.order;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findAllWithFetch() {
        return queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .fetchResults()
                .getResults();
    }
}
