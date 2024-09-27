package com.loulysoft.moneytransfer.accounting.services;

import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import java.math.BigDecimal;

public interface CashInService {

    TransactionReport calculerDetailsMontant(
            Long userId,
            Long companyId,
            // String paysSource,
            String paysDest,
            String service,
            String natureMontant,
            BigDecimal montant);
}
