package com.loulysoft.moneytransfer.accounting.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrilleItem {
    private Long id;

    private Integer sequence;

    private BigDecimal borneInf;

    private BigDecimal borneSup;

    private BigDecimal value;

    private BigDecimal marge;

    private Character pourcentage;

    private Grille grille;
}
