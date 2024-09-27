package com.loulysoft.moneytransfer.accounting.enums;

import java.text.MessageFormat;

public enum Code {
    PRINCIPAL("0000"),
    TIERCE_PRINCIPAL("0001"),
    TAXES("0002"),
    TIMBRE("0003"),
    COMMISSION_HORS_TAXE("0004"),
    COMMISSION_TAXE("0005"),
    ATTENTE_TRANSFERT("0006"),
    PROVISION("0007"),
    ENCAISSEMENT_CASH("0008"),
    DECAISSEMENT_CASH("0013"),
    REVERSE_DECAISSEMENT_CASH("0015"),
    FRAIS("0009"),
    DETTE("0010"),
    CREANCE("0011"),
    COMMISSION_TTC("0012"),
    VERSEMENT_COMPTE("0014"),
    MISE_A_DISPOSITION("0016"),
    AUTRES_FRAIS("0017");

    private String value;

    Code(String value) {
        this.value = value;
    }

    public static Code fromValue(String value) {
        for (Code code : values()) {
            if (code.value.equalsIgnoreCase(value)) {
                return code;
            }
        }
        throw new IllegalArgumentException(
                MessageFormat.format("{0} not found with the value: {1} in [{2}]", Code.class, value, values()));
    }
}
