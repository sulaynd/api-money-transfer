package com.loulysoft.moneytransfer.accounting.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public record TransferRequest(
        @Valid @NotEmpty(message = "user Id cannot be empty") Long userId,
        @Valid @NotEmpty(message = "Transaction Id cannot be empty") Long transactionId,
        @Valid @NotEmpty(message = "Company Id cannot be empty") Long companyId,
        @Valid @NotEmpty(message = "Destination country cannot be empty") String destination,
        @Valid @NotEmpty(message = "Service cannot be empty") String service,
        @Valid @NotEmpty(message = "Nature cannot be empty") String nature,
        @Valid @NotEmpty(message = "Amount cannot be empty") BigDecimal montant) {}
