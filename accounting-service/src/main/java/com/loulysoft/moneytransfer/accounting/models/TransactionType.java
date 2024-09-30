package com.loulysoft.moneytransfer.accounting.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionType {

    private String code;
    private String description, className;
    private Character balanceControl;
}
