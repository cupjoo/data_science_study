package com.restfulshop.server.domain.member;

import lombok.*;

import javax.persistence.Embeddable;

@EqualsAndHashCode(of = {"city", "street", "zipcode"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

    @Builder
    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public void changeAddress(Address address){
        this.city = address.getCity();
        this.street = address.getStreet();
        this.zipcode = address.getZipcode();
    }
}
