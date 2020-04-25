package com.restfulshop.server.service;

import com.restfulshop.server.api.dto.item.*;
import com.restfulshop.server.domain.item.Item;
import com.restfulshop.server.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long create(ItemCreateRequest request){
        Item item = itemRepository.save(request.toEntity());
        return item.getId();
    }

    public ItemResponse findById(Long id){
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item "+id+" does not exist."));
        return new ItemResponse(item);  // Entity to Dto
    }

    public ItemResponseList findAll(){
        return new ItemResponseList(itemRepository.findAll().stream()
                .map(ItemResponse::new)  // Entity to Dto
                .collect(toList()));
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
