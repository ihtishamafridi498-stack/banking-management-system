package com.afridi.bankmanagementsystem.exception;

public class CustomerCnicAlreadyExistsException extends RuntimeException {
    public CustomerCnicAlreadyExistsException(String message) {
        super(message);
    }
}
