package com.loulysoft.moneytransfer.accounting.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class TransactionException extends RrsException {

    public TransactionException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
