package com.restfulshop.server.service;

import com.restfulshop.server.domain.item.*;
import com.restfulshop.server.domain.member.*;
import com.restfulshop.server.domain.order.*;
import com.restfulshop.server.repository.item.ItemRepository;
import com.restfulshop.server.repository.member.MemberRepository;
import com.restfulshop.server.repository.order.OrderItemRepository;
import com.restfulshop.server.repository.order.OrderQueryRepository;
import com.restfulshop.server.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, List<Long> itemIds, List<Integer> counts){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member "+memberId+" does not exist."));
        Delivery delivery = Delivery.builder().address(member.getAddress()).build();

        List<Item> items = itemRepository.findSelectedItems(itemIds);
        List<OrderItem> orderItems = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            OrderItem oi = OrderItem.builder()
                    .item(item)
                    .orderPrice(item.getPrice())
                    .count(counts.get(i)).build();
            orderItemRepository.save(oi);
            orderItems.add(oi);
        }

        Order order = Order.builder()
                .member(member).delivery(delivery).orderItems(orderItems).build();
        orderRepository.save(order);
        return order.getId();
    }

    public Order findById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order "+id+" does not exist."));
    }

    public List<Order> findAllWithFetch(){
        return orderQueryRepository.findAllWithFetch();
    }
    
    @Transactional
    public void completeDelivery(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order " + id + " does not exist."));
        order.completeDelivery();
    }

    @Transactional
    public void changeDeliveryAddress(Long id, Address address){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order " + id + " does not exist."));
        order.changeDeliveryAddress(address);
    }

    @Transactional
    public void cancel(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order "+id+" does not exist."));
        order.cancel();
    }
}
