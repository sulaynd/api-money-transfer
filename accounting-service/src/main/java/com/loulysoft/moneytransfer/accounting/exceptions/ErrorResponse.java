package com.loulysoft.moneytransfer.accounting.exceptions;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ErrorResponse {

    private Integer httpCode;

    private String message;

    private String errorCode;

    private UUID responseId;

    private Instant timestamp;
}
