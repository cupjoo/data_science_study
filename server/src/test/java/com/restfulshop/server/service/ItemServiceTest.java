package com.restfulshop.server.service;

import com.restfulshop.server.api.dto.item.ItemResponse;
import com.restfulshop.server.api.dto.item.ItemListResponse;
import com.restfulshop.server.domain.item.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Test
    void basicCrud() {
        // create
        Item item1 = Item.builder().name("icecream").price(1500).build();
        item1.addStock(300);
        Item item2 = Item.builder().name("chicken").price(20000).build();
        item2.addStock(20);

        itemService.create(item1);
        itemService.create(item2);

        // read
        ItemListResponse itemList = itemService.findAll();
        ItemResponse response = itemList.getData().get(0);

        assertThat(itemList.getCount()).isEqualTo(2);
        assertThat(response.getName()).isEqualTo(item1.getName());

        // update
        itemService.updatePriceAndStock(response.getId(), 2000, -10);
        ItemResponse updateItem = itemService.findById(response.getId());
        assertThat(updateItem.getPrice()).isEqualTo(2000);
        assertThat(updateItem.getStockQuantity()).isEqualTo(290);

        // delete
        itemService.delete(response.getId());
        assertThatThrownBy(() -> itemService.findById(response.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Item "+response.getId()+" does not exist.");
    }
}