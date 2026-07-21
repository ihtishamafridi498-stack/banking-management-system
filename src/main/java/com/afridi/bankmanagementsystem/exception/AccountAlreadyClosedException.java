package com.afridi.bankmanagementsystem.exception;

public class AccountAlreadyClosedException extends RuntimeException {

    public AccountAlreadyClosedException(String message) {
        super(message);
    }
}
