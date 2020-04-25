package com.restfulshop.server.domain.member;

import com.restfulshop.server.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @Builder
    public Member(String name, Address address){
        this.name = name;
        this.address = address;
    }

    public void changeName(String name){
        this.name = name;
    }
}
