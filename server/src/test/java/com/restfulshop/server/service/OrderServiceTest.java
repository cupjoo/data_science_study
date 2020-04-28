package com.restfulshop.server.service;

import com.restfulshop.server.domain.item.Item;
import com.restfulshop.server.domain.member.*;
import com.restfulshop.server.domain.order.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Test
    void order() {
        Member member = createMember();
        List<Item> items = Arrays.asList(
                createItem(1, 3000, 50),
                createItem(2, 5000, 20));
        List<Long> itemIds = items.stream().map(Item::getId).collect(toList());
        List<Integer> counts = items.stream().map(Item::getStockQuantity).collect(toList());
        int totalPrice = 50 * (items.get(0).getPrice()) + 20 * items.get(1).getPrice();

        Long orderId = orderService.order(member.getId(), itemIds, counts);
        Order order = orderService.findById(orderId);

        assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(order.getOrderItems().get(0).getItem()).isEqualTo(items.get(0));
    }

    @Test
    void completeDelivery(){
        Order order = createOrder();
        orderService.completeDelivery(order.getId());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
    }

    @Test
    void changeDeliveryAddress(){
        Address address = Address.builder()
                .city("Seoul").street("Ssangmun").zipcode("10101").build();
        Order order = createOrder();
        orderService.changeDeliveryAddress(order.getId(), address);
        assertThat(order.getDelivery().getAddress()).isEqualTo(address);
    }

    @Test
    void cancel(){
        List<Item> items = Arrays.asList(
                createItem(1, 3000, 50),
                createItem(2, 5000, 20));
        Order order = createOrder(items);
        orderService.cancel(order.getId());

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(items.get(0).getStockQuantity()).isEqualTo(50);
        assertThat(items.get(1).getStockQuantity()).isEqualTo(20);

        assertThatThrownBy(order::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 취소된 주문입니다.");
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
        List<Item> items = Arrays.asList(
                createItem(1, 3000, 50),
                createItem(2, 5000, 20));
        return createOrder(items);
    }
    private Order createOrder(List<Item> items){
        List<Long> itemIds = items.stream().map(Item::getId).collect(toList());
        List<Integer> counts = items.stream().map(Item::getStockQuantity).collect(toList());
        Address address = Address.builder()
                .city("Busan").street("Donggu").zipcode("20202").build();

        Long orderId = orderService.order(createMember().getId(), itemIds, counts);
        return orderService.findById(orderId);
    }
}