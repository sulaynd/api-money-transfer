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
public class ValeurParametre {

    private Long id;

    private BigDecimal value;

    private Long companyId;

    private Parametre param;

    private Grille grille;

    private String paysCode;

    // private Devise devise;
}
