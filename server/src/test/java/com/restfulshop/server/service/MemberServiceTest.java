package com.restfulshop.server.service;

import com.restfulshop.server.api.dto.member.MemberCreateRequest;
import com.restfulshop.server.api.dto.member.MemberResponse;
import com.restfulshop.server.api.dto.member.MemberResponseList;
import com.restfulshop.server.domain.member.Address;
import com.restfulshop.server.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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
        MemberCreateRequest request1 = MemberCreateRequest.builder()
                .name("Junyoung").address(address1).build();
        MemberCreateRequest request2 = MemberCreateRequest.builder()
                .name("JunHyuk").address(address2).build();

        memberService.create(request1);
        memberService.create(request2);

        // read
        MemberResponseList memberList = memberService.findAll();
        MemberResponse response = memberList.getData().get(0);

        assertThat(memberList.getCount()).isEqualTo(2);
        assertThat(response.getName()).isEqualTo(request1.getName());

        // update
        memberService.update(response.getId(), "James");
        MemberResponse updateMember = memberService.findById(response.getId());
        assertThat(updateMember.getName()).isEqualTo("James");

        // delete
        memberService.delete(response.getId());
        assertThatThrownBy(() -> memberService.findById(response.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Member "+response.getId()+" does not exist.");
    }
}