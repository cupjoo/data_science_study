package com.restfulshop.server.domain.item;

import com.restfulshop.server.exception.NotEnoughPriceException;
import com.restfulshop.server.exception.NotEnoughStockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class ItemTest {

    @Autowired
    EntityManager em;

    @Test
    void create(){
        Item item = Item.builder()
                .name("keyboard").price(10000).build();
        item.addStock(10);
        em.persist(item);

        Item findItem = em.find(Item.class, item.getId());

        assertThat(findItem.getName()).isEqualTo("keyboard");
        assertThat(findItem.getStockQuantity()).isEqualTo(10);
    }

    @Test
    void update() {
        Item item = Item.builder()
                .name("keyboard").price(10000).build();
        item.addStock(10);
        em.persist(item);

        Item findItem = em.find(Item.class, item.getId());

        assertThatThrownBy(() -> findItem.removeStock(100))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("not enough stock: " + 90);
        assertThatThrownBy(() -> findItem.changePrice(0))
                .isInstanceOf(NotEnoughPriceException.class)
                .hasMessage("can't decrease price: " + 0);
    }
}