package com.restfulshop.server.domain.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restfulshop.server.domain.item.Item;
import com.restfulshop.server.domain.member.Address;
import com.restfulshop.server.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.*;

import static com.restfulshop.server.domain.member.QMember.*;
import static com.restfulshop.server.domain.order.QDelivery.*;
import static com.restfulshop.server.domain.order.QOrder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class OrderTest {

    @Autowired
    EntityManager em;

    @Autowired
    JPAQueryFactory queryFactory;

    @Test
    void orderCreate(){
        // given
        Member member1 = createMember();
        Item item1 = createItem(1, 3000, 50);
        Item item2 = createItem(2, 5000, 20);
        OrderItem orderItem1 = OrderItem.builder()
                .item(item1).count(15).orderPrice(item1.getPrice()-500).build();
        OrderItem orderItem2 = OrderItem.builder()
                .item(item2).count(20).orderPrice(item2.getPrice()).build();
        int totalPrice = 15 * (item1.getPrice()-500) + 20 * item2.getPrice();

        Delivery delivery1 = Delivery.builder().address(member1.getAddress()).build();
        Order order1 = Order.builder()
                .member(member1).delivery(delivery1)
                .orderItems(Arrays.asList(orderItem1, orderItem2)).build();
        em.persist(order1);
        em.flush();

        // when
        Order findOrder = Optional.ofNullable(queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .where(order.id.eq(order1.getId()))
                .fetchOne())
                .orElseThrow(() -> new IllegalArgumentException("Order not found."));
        Item findItem = em.find(Item.class, item1.getId());

        // then
        assertThat(findOrder.getId()).isEqualTo(order1.getId());
        assertThat(findOrder.getDelivery()).isEqualTo(delivery1);
        assertThat(findOrder.getMember()).isEqualTo(member1);
        assertThat(findOrder.getStatus()).isEqualByComparingTo(OrderStatus.ORDER);
        assertThat(findOrder.getTotalPrice()).isEqualTo(totalPrice);

        OrderItem findOrderItem = findOrder.getOrderItems().get(0);
        assertThat(findOrderItem.getOrderPrice()).isEqualTo(item1.getPrice()-500);
        assertThat(findOrderItem.getItem().getId()).isEqualTo(item1.getId());

        assertThat(findItem.getStockQuantity()).isEqualTo(35);
    }

    @Test
    void cancelOrder() {
        Member member1 = createMember();
        Item item1 = createItem(1, 3000, 50);
        Item item2 = createItem(2, 5000, 20);
        OrderItem orderItem1 = OrderItem.builder()
                .item(item1).count(15).orderPrice(item1.getPrice()-500).build();
        OrderItem orderItem2 = OrderItem.builder()
                .item(item2).count(20).orderPrice(item2.getPrice()).build();
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem1);

        Delivery delivery1 = Delivery.builder().address(member1.getAddress()).build();
        Order order1 = Order.builder()
                .member(member1).delivery(delivery1)
                .orderItems(Arrays.asList(orderItem1, orderItem2)).build();
        em.persist(order1);

        assertThat(item1.getStockQuantity()).isEqualTo(35);
        order1.cancel();

        assertThat(order1.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(item1.getStockQuantity()).isEqualTo(50);
    }

    @Test
    void completeDelivery() {
        Order order1 = createOrder();
        order1.completeDelivery();

        assertThat(order1.getDelivery().getStatus()).isEqualTo(DeliveryStatus.COMP);

        em.clear();
        Order order2 = createOrder();
        order2.cancel();

        assertThatThrownBy(order2::completeDelivery)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 취소된 주문입니다.");
    }

    @Test
    void changeDeliveryAddress() {
        Address address = Address.builder()
                .city("Busan").street("Donggu").zipcode("10101").build();
        Order order1 = createOrder();
        order1.changeDeliveryAddress(address);

        assertThat(order1.getDelivery().getAddress()).isEqualTo(address);

        order1.completeDelivery();
        assertThatThrownBy(() -> order1.changeDeliveryAddress(address))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 배송완료된 주문입니다.");
    }

    private Member createMember(){
        Address address = Address.builder()
                .city("Seoul").street("Ssangmun").zipcode("10101").build();
        Member member = Member.builder().name("Junyoung").address(address).build();
        em.persist(member);
        return member;
    }
    private Item createItem(int idx, int price, int stock){
        Item item = Item.builder().name("item"+idx).price(price).build();
        item.addStock(stock);
        em.persist(item);
        return item;
    }
    private Order createOrder(){
        Member member1 = createMember();
        Item item1 = createItem(1, 3000, 50);
        Item item2 = createItem(2, 5000, 20);
        OrderItem orderItem1 = OrderItem.builder()
                .item(item1).count(15).orderPrice(item1.getPrice()-500).build();
        OrderItem orderItem2 = OrderItem.builder()
                .item(item2).count(20).orderPrice(item2.getPrice()).build();
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem1);

        Delivery delivery1 = Delivery.builder().address(member1.getAddress()).build();
        Order order1 = Order.builder()
                .member(member1).delivery(delivery1)
                .orderItems(Arrays.asList(orderItem1, orderItem2)).build();
        em.persist(order1);
        return order1;
    }
}