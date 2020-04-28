package com.restfulshop.server.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    public void basicCrud(){
        Address address = Address.builder()
                .city("Seoul").street("Ssangmun").zipcode("10101").build();
        Member member = Member.builder().name("Junyoung").address(address).build();
        em.persist(member);

        Member findMember = em.find(Member.class, member.getId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getAddress()).isEqualTo(address);

        findMember.changeName("JunHyuk");
        Address updateAddress = Address.builder()
                .city("Busan").street("Donggu").zipcode("20202").build();
        findMember.getAddress().changeAddress(updateAddress);
        em.flush();
        em.clear();

        Member updateMember = em.find(Member.class, member.getId());
        assertThat(updateMember.getName()).isEqualTo(member.getName());
        assertThat(updateMember.getAddress()).isEqualTo(updateAddress);
    }
}