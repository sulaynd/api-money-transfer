package com.loulysoft.moneytransfer.accounting.services;

import com.loulysoft.moneytransfer.accounting.models.TransactionReport;

public interface TransactionService {

    TransactionReport finaliserTransaction(Long userId, Long companyId, Long reference);

    TransactionReport deroulerTransaction(Long userId, Long companyId, Long transactionId);

    TransactionReport createTransaction(Long userId, Long companyId, Long reference);
}
