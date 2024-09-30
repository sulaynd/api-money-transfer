package com.loulysoft.moneytransfer.accounting.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class ForbiddenActionException extends RrsException {

    public ForbiddenActionException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
