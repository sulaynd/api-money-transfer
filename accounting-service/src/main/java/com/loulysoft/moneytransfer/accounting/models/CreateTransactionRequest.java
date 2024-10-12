package com.loulysoft.moneytransfer.accounting.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateTransactionRequest(
        @Valid @NotNull(message = "User cannot be empty") Long userId,
        @Valid @NotNull(message = "Company cannot be empty") Long companyId,
        @Valid @NotNull(message = "Reference cannot be empty") Long reference) {}
