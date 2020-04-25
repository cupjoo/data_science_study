package com.restfulshop.server.service;

import com.restfulshop.server.api.dto.item.ItemCreateRequest;
import com.restfulshop.server.api.dto.item.ItemResponse;
import com.restfulshop.server.api.dto.item.ItemResponseList;
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
        ItemCreateRequest request1 = ItemCreateRequest.builder()
                .name("icecream").price(1500).stockQuantity(300).build();
        ItemCreateRequest request2 = ItemCreateRequest.builder()
                .name("chicken").price(20000).stockQuantity(20).build();

        itemService.create(request1);
        itemService.create(request2);

        // read
        ItemResponseList itemList = itemService.findAll();
        ItemResponse response = itemList.getData().get(0);

        assertThat(itemList.getCount()).isEqualTo(2);
        assertThat(response.getName()).isEqualTo(request1.getName());

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