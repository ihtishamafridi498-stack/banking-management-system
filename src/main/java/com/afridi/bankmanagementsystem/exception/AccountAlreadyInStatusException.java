package com.afridi.bankmanagementsystem.exception;

public class AccountAlreadyInStatusException extends RuntimeException {

  public AccountAlreadyInStatusException(String message) {
    super(message);
  }
}
