package com.restfulshop.server.api.dto.item;

import com.restfulshop.server.domain.item.Item;
import lombok.*;

@Getter
@NoArgsConstructor
public class ItemCreateRequest {

    private String name;
    private int price;
    private int stockQuantity;

    @Builder
    public ItemCreateRequest(String name, int price, int stockQuantity){
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Item toEntity(){
        Item item = Item.builder().name(name).price(price).build();
        item.addStock(stockQuantity);
        return item;
    }
}
