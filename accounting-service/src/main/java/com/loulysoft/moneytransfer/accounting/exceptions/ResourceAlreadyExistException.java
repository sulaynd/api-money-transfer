package com.loulysoft.moneytransfer.accounting.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class ResourceAlreadyExistException extends RrsException {

    public ResourceAlreadyExistException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
