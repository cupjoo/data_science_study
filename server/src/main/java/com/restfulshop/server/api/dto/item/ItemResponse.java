package com.restfulshop.server.api.dto.item;

import com.restfulshop.server.domain.item.Item;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ItemResponse {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    public ItemResponse(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stockQuantity = item.getStockQuantity();
    }
}
