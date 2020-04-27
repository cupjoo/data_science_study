package com.restfulshop.server.domain.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void findSelectedItems(){
        List<Item> items = new ArrayList<>();
        for(int i = 1; i <= 10; i++){
            items.add(getItem(i));
        }

        List<Long> itemIds = items.stream()
                .filter(i -> i.getName().equals("item1") || i.getName().equals("item5"))
                .map(Item::getId)
                .collect(toList());
        List<Item> selectedItems = itemRepository.findSelectedItems(itemIds);
        assertThat(selectedItems).extracting("name").contains("item1", "item5");
    }

    private Item getItem(int idx){
        Item item = Item.builder().name("item"+idx).price(1000*idx).build();
        item.addStock(100*idx);
        itemRepository.save(item);
        return item;
    }
}