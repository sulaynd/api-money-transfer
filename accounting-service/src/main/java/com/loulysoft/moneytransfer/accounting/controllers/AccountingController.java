package com.loulysoft.moneytransfer.accounting.controllers;

import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.services.CashInService;
import com.loulysoft.moneytransfer.accounting.services.TransactionService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounting")
public class AccountingController {

    private final CashInService cashInService;
    private final TransactionService transactionService;

    @GetMapping(value = "/ratings")
    TransactionReport getRatings(
            @RequestParam(name = "user") Long userId,
            @RequestParam(name = "company") Long companyId,
            @RequestParam(name = "destination") String destination,
            @RequestParam(name = "service") String service,
            @RequestParam(name = "nature") String nature,
            @RequestParam(name = "montant") BigDecimal montant) {

        return cashInService.calculerDetailsMontant(userId, companyId, destination, service, nature, montant);
    }

    @GetMapping(value = "/transactions")
    TransactionReport createTransaction(
            @RequestParam(name = "user") Long userId,
            @RequestParam(name = "company") Long companyId,
            @RequestParam(name = "reference") Long reference) {

        return transactionService.finaliserTransaction(userId, companyId, reference);
    }
}
