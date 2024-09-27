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
public class CoursDevise {

    private Long id;

    private Devise source;

    private Devise cible;

    private BigDecimal facteur;

    private BigDecimal marge;

    private BigDecimal coursPratique;

    CoursDeviseTemplate templateCoursDevise;
}
