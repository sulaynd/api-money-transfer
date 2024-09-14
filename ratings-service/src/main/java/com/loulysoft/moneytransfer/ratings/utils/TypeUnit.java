package com.loulysoft.moneytransfer.ratings.utils;

import java.text.MessageFormat;

public enum TypeUnit {
    SYSTEME("00"),
    SYSTEME_ZONE("01"),
    BANQUE("02"),
    COMPAGNIE("03"),
    PARTENAIRE_EXTERNE("04"),
    PARTENAIRE_INTERNE("05"),
    DISTRIBUTEUR("06"),
    POS("07"),
    AGENCE_BANQUE("08");

    private String value;

    TypeUnit(String value) {
        this.value = value;
    }

    public static TypeUnit fromValue(String value) {
        for (TypeUnit unit : values()) {
            if (unit.value.equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw new IllegalArgumentException(
                MessageFormat.format("{0} not found with the value: {1} in [{2}]", TypeUnit.class, value, values()));
    }
}
