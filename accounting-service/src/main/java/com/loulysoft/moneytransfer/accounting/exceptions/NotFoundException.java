package com.loulysoft.moneytransfer.accounting.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class NotFoundException extends RrsException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
