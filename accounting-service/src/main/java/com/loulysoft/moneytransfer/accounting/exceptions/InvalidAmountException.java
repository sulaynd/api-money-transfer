package com.loulysoft.moneytransfer.accounting.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidAmountException extends RrsException {

    public InvalidAmountException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
