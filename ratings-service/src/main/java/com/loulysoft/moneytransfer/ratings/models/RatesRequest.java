package com.loulysoft.moneytransfer.ratings.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record RatesRequest(
        @Valid @NotEmpty(message = "Source country cannot be empty") String SourceCountry,
        @Valid @NotEmpty(message = "Destination country cannot be empty") String destinationCountry) {}
