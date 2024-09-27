package com.loulysoft.moneytransfer.accounting.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class InternalException extends RrsException {

    public InternalException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
