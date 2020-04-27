package com.restfulshop.server.domain.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.restfulshop.server.domain.item.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Item> findSelectedItems(List<Long> itemIds) {
        return queryFactory
                .selectFrom(item)
                .where(item.id.in(itemIds))
                .fetchResults()
                .getResults();
    }
}
