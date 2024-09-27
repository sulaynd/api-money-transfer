package com.loulysoft.moneytransfer.accounting.controllers;

import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.services.CashInService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounting")
public class RateAndFeesController {

    private final CashInService cashInService;

    @GetMapping
    TransactionReport getRateAndFees(
            @RequestParam(name = "user") Long userId,
            @RequestParam(name = "company") Long companyId,
            @RequestParam(name = "destination") String destination,
            @RequestParam(name = "service") String service,
            @RequestParam(name = "nature") String nature,
            @RequestParam(name = "montant") BigDecimal montant) {

        return cashInService.calculerDetailsMontant(userId, companyId, destination, service, nature, montant);
    }
}
