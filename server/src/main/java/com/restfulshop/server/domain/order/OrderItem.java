package com.restfulshop.server.domain.order;

import com.restfulshop.server.domain.BaseTimeEntity;
import com.restfulshop.server.domain.item.Item;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    @Builder
    public OrderItem(Item item, int orderPrice, int count){
        changeItem(item);
        changeOrderPrice(orderPrice);
        changeCount(count);

        item.removeStock(count);
    }

    public void changeItem(Item item){
        this.item = item;
    }
    public void changeOrder(Order order){
        this.order = order;
    }

    public void changeOrderPrice(int orderPrice){
        this.orderPrice = orderPrice;
    }

    public void changeCount(int count){
        this.count = count;
    }

    public void cancel(){
        item.addStock(count);
    }

    public int getTotalPrice(){
        return orderPrice * count;
    }
}
