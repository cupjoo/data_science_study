package com.restfulshop.server.service;

import com.restfulshop.server.domain.item.Item;
import com.restfulshop.server.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long create(Item item){
        itemRepository.save(item);
        return item.getId();
    }

    public Item findById(Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item "+id+" does not exist."));
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    @Transactional
    public void updatePriceAndStock(Long id, int price, int amount){
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item "+id+" does not exist."));
        item.changePrice(price);
        if(amount < 0){
            item.removeStock(-amount);
        } else {
            item.addStock(amount);
        }
    }

    @Transactional
    public void delete(Long id){
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item "+id+" does not exist."));
        itemRepository.delete(item);
    }
}
