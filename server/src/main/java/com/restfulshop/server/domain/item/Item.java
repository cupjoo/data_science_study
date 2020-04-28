package com.restfulshop.server.domain.item;

import com.restfulshop.server.domain.BaseTimeEntity;
import com.restfulshop.server.exception.NotEnoughPriceException;
import com.restfulshop.server.exception.NotEnoughStockException;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @Builder
    public Item(String name, int price){
        changeName(name);
        changePrice(price);
    }

    public void changeName(String name){
        this.name = name;
    }

    public void changePrice(int price) {
        if(price <= 0){
            throw new NotEnoughPriceException(price);
        }
        this.price = price;
    }

    public void addStock(int quantity){
        if(quantity < 0){
            removeStock(-quantity);
        }
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException(restStock);
        }
        this.stockQuantity = restStock;
    }
}
