package com.loulysoft.moneytransfer.accounting.enums;

public enum Niveau {
    ZERO(0),
    UN(1),
    DEUX(2),
    TROIS(3),
    QUATRE(4);

    final int value;

    Niveau(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
