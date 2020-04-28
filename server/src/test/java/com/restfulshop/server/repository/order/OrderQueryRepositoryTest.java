package com.restfulshop.server.repository.order;

import com.restfulshop.server.api.dto.order.OrderSearchCondition;
import com.restfulshop.server.domain.item.Item;
import com.restfulshop.server.domain.member.*;
import com.restfulshop.server.domain.order.*;
import com.restfulshop.server.domain.order.Order;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.*;
import java.util.stream.IntStream;

@Transactional
@SpringBootTest
class OrderQueryRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderQueryRepository orderQueryRepository;

    @Test
    void findAllWithPagination() {
        OrderSearchCondition blankCondition = new OrderSearchCondition();
        OrderSearchCondition condition = OrderSearchCondition.builder()
                .memberName("Junyoung").orderStatus(OrderStatus.ORDER).build();
        PageRequest pageRequest = PageRequest.of(0, 5);

        Page<Order> blankPage = orderQueryRepository.findAllWithPagination(
                blankCondition, pageRequest);

        Page<Order> page = orderQueryRepository.findAllWithPagination(condition, pageRequest);
    }

    @BeforeEach
    void initOrders(){
        Member member1 = createMember("Junyoung");
        Member member2 = createMember("Junhyuk");

        IntStream.range(0, 12).forEach(i -> {
            Order order = createOrder(member1);
            if (i % 2 == 0) order.cancel();
        });
        IntStream.range(0, 12).forEach(i -> {
            Order order = createOrder(member2);
            if (i % 2 == 0) order.cancel();
        });
    }

    private Member createMember(String name){
        Address address = Address.builder()
                .city("Seoul").street("Ssangmun").zipcode("10101").build();
        Member member = Member.builder().name(name).address(address).build();
        em.persist(member);
        return member;
    }

    private Item createItem(int idx, int price, int stock){
        Item item = Item.builder().name("item"+idx).price(price).build();
        item.addStock(stock);
        em.persist(item);
        return item;
    }

    private Order createOrder(Member member){
        Item item1 = createItem(1, 3000, 50);
        Item item2 = createItem(2, 5000, 20);
        OrderItem orderItem1 = OrderItem.builder()
                .item(item1).count(15).orderPrice(item1.getPrice()-500).build();
        OrderItem orderItem2 = OrderItem.builder()
                .item(item2).count(20).orderPrice(item2.getPrice()).build();
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem1);

        Delivery delivery1 = Delivery.builder().address(member.getAddress()).build();
        Order order1 = Order.builder()
                .member(member).delivery(delivery1)
                .orderItems(Arrays.asList(orderItem1, orderItem2)).build();
        em.persist(order1);
        return order1;
    }
}