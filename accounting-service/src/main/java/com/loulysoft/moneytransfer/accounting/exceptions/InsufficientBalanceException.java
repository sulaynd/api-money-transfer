package com.loulysoft.moneytransfer.accounting.exceptions;

import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends RrsException {

    public InsufficientBalanceException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
