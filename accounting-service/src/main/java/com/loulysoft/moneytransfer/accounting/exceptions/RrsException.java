package com.loulysoft.moneytransfer.accounting.exceptions;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RrsException extends RuntimeException {

    private UUID id;
    private HttpStatus httpStatus;
    private String message;

    public RrsException(HttpStatus httpStatus, String message) {
        this.id = UUID.randomUUID();
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
