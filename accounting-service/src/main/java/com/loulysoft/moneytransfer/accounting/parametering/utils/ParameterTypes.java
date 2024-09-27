package com.loulysoft.moneytransfer.accounting.parametering.utils;

import lombok.Getter;

@Getter
public enum ParameterTypes {
    SIMPLE("SIMPLE", "com.loulysoft.moneytransfer.accounting.parametering.utils.SimpleParam"),
    COUNTRY("COUNTRY", "com.loulysoft.moneytransfer.accounting.parametering.utils.CountryParam"),
    // MONETARY_ZONE("MONETARY_ZONE", "com.loulysoft.moneytransfer.accounting.parametering.utils.MonetaryZoneParam"),
    CORRIDOR("CORRIDOR", "com.loulysoft.moneytransfer.accounting.parametering.utils.CorridorParam"),
    TRANSACTION_CONTEXT(
            "TRANSACTION_CONTEXT", "com.loulysoft.moneytransfer.accounting.parametering.utils.TransactionContextParam"),
    SERVICE_CONTEXT("SERVICE_CONTEXT", "com.loulysoft.moneytransfer.accounting.parametering.utils.ServiceContextParam"),
    EXCHANGE_RATE("EXCHANGE_RATE", "com.loulysoft.moneytransfer.accounting.parametering.utils.ExchangeRateParam"),
    CALCUL("CALCUL", "com.loulysoft.moneytransfer.accounting.parametering.utils.CalculParam");

    private final String code;
    private final String className;

    ParameterTypes(String code, String className) {
        this.code = code;
        this.className = className;
    }
}
