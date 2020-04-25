package com.restfulshop.server.exception;

public class MemberDoesNotExistException extends RuntimeException {

    public MemberDoesNotExistException(Long id){
        super("Member "+id+" does not exist.");
    }
}
