package com.loulysoft.moneytransfer.accounting.controllers;

import com.loulysoft.moneytransfer.accounting.models.CreateTransactionRequest;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.services.CashInService;
import com.loulysoft.moneytransfer.accounting.services.TransactionService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping(value = "/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    TransactionReport createTransaction(@Valid @RequestBody CreateTransactionRequest request) {

        return transactionService.createTransaction(request);
    }
}
