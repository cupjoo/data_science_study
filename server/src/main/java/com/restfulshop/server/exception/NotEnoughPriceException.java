package com.restfulshop.server.exception;

public class NotEnoughPriceException extends RuntimeException {

    public NotEnoughPriceException(int price) {
        super("can't decrease price: "+ price);
    }
}
