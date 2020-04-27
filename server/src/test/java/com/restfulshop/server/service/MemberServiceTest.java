package com.restfulshop.server.service;

import com.restfulshop.server.domain.member.Address;
import com.restfulshop.server.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void basicCrud() {
        // create
        Address address1 = Address.builder()
                .city("Seoul").street("Ssangmun").zipcode("10101").build();
        Address address2 = Address.builder()
                .city("Busan").street("Donggu").zipcode("20202").build();
        Member member1 = Member.builder()
                .name("Junyoung").address(address1).build();
        Member member2 = Member.builder()
                .name("JunHyuk").address(address2).build();

        memberService.create(member1);
        memberService.create(member2);

        // read
        List<Member> members = memberService.findAll();
        Member member = members.get(0);

        assertThat(members.size()).isEqualTo(2);
        assertThat(member.getName()).isEqualTo(member1.getName());

        // update
        memberService.update(member.getId(), "James");
        Member updateMember = memberService.findById(member.getId());
        assertThat(updateMember.getName()).isEqualTo("James");

        // delete
        memberService.delete(member.getId());
        assertThatThrownBy(() -> memberService.findById(member.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Member "+member.getId()+" does not exist.");
    }
}