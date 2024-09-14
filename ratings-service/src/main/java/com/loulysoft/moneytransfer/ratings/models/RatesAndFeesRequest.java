package com.loulysoft.moneytransfer.ratings.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public record RatesAndFeesRequest(
        @Valid @NotEmpty(message = "Source country cannot be empty") String SourceCountry,
        @Valid @NotEmpty(message = "Destination country cannot be empty") String destinationCountry,
        @Valid String serviceCode,
        @Valid BigDecimal amount) {}
