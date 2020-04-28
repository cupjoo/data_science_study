package com.restfulshop.server.repository.item;

import com.restfulshop.server.domain.item.Item;

import java.util.List;

public interface ItemQueryRepository {

    List<Item> findSelectedItems(List<Long> itemIds);
}
