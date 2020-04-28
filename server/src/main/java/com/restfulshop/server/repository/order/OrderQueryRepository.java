package com.restfulshop.server.repository.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restfulshop.server.api.dto.order.OrderSearchCondition;
import com.restfulshop.server.domain.order.Order;
import com.restfulshop.server.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.restfulshop.server.domain.member.QMember.member;
import static com.restfulshop.server.domain.order.QDelivery.delivery;
import static com.restfulshop.server.domain.order.QOrder.order;
import static org.springframework.util.StringUtils.isEmpty;

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

    public Page<Order> findAllWithPagination(
            OrderSearchCondition condition,
            Pageable pageable){

        List<Order> content = queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .where(memberNameEq(condition.getMemberName()),
                        orderStatusEq(condition.getOrderStatus()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Order> countQuery = queryFactory
                .selectFrom(order)
                .where(memberNameEq(condition.getMemberName()),
                        orderStatusEq(condition.getOrderStatus()));

        return PageableExecutionUtils.getPage(
                content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression memberNameEq(String memberName){
        return isEmpty(memberName) ? null : member.name.eq(memberName);
    }

    private BooleanExpression orderStatusEq(OrderStatus orderStatus){
        return isEmpty(orderStatus) ? null : order.status.eq(orderStatus);
    }
}
