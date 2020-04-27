package com.restfulshop.server.domain.order;

import com.restfulshop.server.domain.BaseTimeEntity;
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
        changeMember(member);
        changeDelivery(delivery);
        for (OrderItem orderItem: orderItems){
            addOrderItem(orderItem);
        }
        changeStatus(OrderStatus.ORDER);
        changeOrderDate(LocalDateTime.now());
    }

    public void changeMember(Member member){
        this.member = member;
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.changeOrder(this);
    }

    public void changeDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.changeOrder(this);
    }

    private void changeStatus(OrderStatus status){
        this.status = status;
    }

    public void changeOrderDate(LocalDateTime orderDate){
        this.orderDate = orderDate;
    }

    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송이 완료된 상품은 취소가 불가능합니다.");
        }
        changeStatus(OrderStatus.CANCEL);
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
