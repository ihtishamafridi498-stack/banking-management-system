package com.afridi.bankmanagementsystem.exception;

public class CustomerHasActiveAccountsException extends RuntimeException {

    public CustomerHasActiveAccountsException(String message) {
        super(message);
    }
}
