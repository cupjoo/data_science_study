package com.restfulshop.server.domain.order;

import com.restfulshop.server.domain.item.Item;
import com.restfulshop.server.domain.member.Address;
import com.restfulshop.server.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.restfulshop.server.domain.member.QMember.member;
import static com.restfulshop.server.domain.order.QDelivery.delivery;
import static com.restfulshop.server.domain.order.QOrder.order;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void createOrder(){
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

        // when
        orderRepository.save(order1);
        Order findOrder = orderRepository.findAllWithFetch().get(0);

        // then
        assertThat(findOrder.getId()).isEqualTo(order1.getId());
        assertThat(findOrder.getDelivery()).isEqualTo(delivery1);
        assertThat(findOrder.getMember()).isEqualTo(member1);
        assertThat(findOrder.getStatus()).isEqualByComparingTo(OrderStatus.ORDER);
        assertThat(findOrder.getTotalPrice()).isEqualTo(totalPrice);

        OrderItem findOrderItem = findOrder.getOrderItems().get(0);
        assertThat(findOrderItem.getOrderPrice()).isEqualTo(item1.getPrice()-500);
        assertThat(findOrderItem.getItem().getId()).isEqualTo(item1.getId());
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
}