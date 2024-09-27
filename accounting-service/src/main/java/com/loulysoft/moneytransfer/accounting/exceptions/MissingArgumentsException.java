package com.loulysoft.moneytransfer.accounting.exceptions;

import org.springframework.http.HttpStatus;

public class MissingArgumentsException extends RrsException {

    public MissingArgumentsException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
