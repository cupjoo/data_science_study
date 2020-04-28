package com.restfulshop.server.exception;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(int rem) {
        super("not enough stock: "+(-rem));
    }
}
