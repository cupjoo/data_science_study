package com.restfulshop.server.domain.order;

import com.restfulshop.server.domain.BaseTimeEntity;
import com.restfulshop.server.domain.member.Address;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Delivery extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Builder
    public Delivery(Address address){
        this.address = address;
        this.status = DeliveryStatus.READY;
    }

    public void changeOrder(Order order){
        this.order = order;
    }

    public void completeDelivery(){
        this.status = DeliveryStatus.COMP;
    }

    public void changeAddress(Address address){
        if(status.equals(DeliveryStatus.COMP)){
            throw new IllegalStateException("이미 배송완료된 주문입니다.");
        } else {
            this.address = address;
        }
    }
}
