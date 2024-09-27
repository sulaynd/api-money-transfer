/**
 *
 */
package com.loulysoft.moneytransfer.accounting.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * @author A748947
 *
 */
@Slf4j
public class UnauthorizedException extends RrsException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
