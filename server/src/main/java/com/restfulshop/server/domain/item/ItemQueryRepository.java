package com.restfulshop.server.domain.item;

import java.util.List;

public interface ItemQueryRepository {

    List<Item> findSelectedItems(List<Long> itemIds);
}
