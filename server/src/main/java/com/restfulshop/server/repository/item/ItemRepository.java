package com.restfulshop.server.repository.item;

import com.restfulshop.server.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemQueryRepository {
}
