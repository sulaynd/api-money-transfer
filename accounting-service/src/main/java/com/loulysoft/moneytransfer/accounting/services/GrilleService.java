package com.loulysoft.moneytransfer.accounting.services;

import java.math.BigDecimal;

public interface GrilleService {
    BigDecimal getGrilleValue(Long companyId, Long grilleId, BigDecimal montant);
}
