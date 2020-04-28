package com.restfulshop.server.domain.order;

import com.restfulshop.server.domain.BaseTimeEntity;
import com.restfulshop.server.domain.member.Address;
import com.restfulshop.server.domain.member.Member;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder
    public Order(Member member, Delivery delivery, List<OrderItem> orderItems){
        this.member = member;
        delivery.changeOrder(this);
        this.delivery = delivery;
        for (OrderItem orderItem: orderItems){
            orderItem.changeOrder(this);
            this.orderItems.add(orderItem);
        }
        this.status = OrderStatus.ORDER;
        this.orderDate = LocalDateTime.now();
    }

    public void completeDelivery(){
        if(status.equals(OrderStatus.CANCEL)){
            throw new IllegalStateException("이미 취소된 주문입니다.");
        } else {
            delivery.completeDelivery();
        }
    }

    public void changeDeliveryAddress(Address address){
        delivery.changeAddress(address);
    }

    public void cancel(){
        this.status = OrderStatus.CANCEL;
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    public int getTotalPrice(){
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
