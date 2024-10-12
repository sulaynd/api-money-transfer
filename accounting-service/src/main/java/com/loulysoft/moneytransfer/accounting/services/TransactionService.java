package com.loulysoft.moneytransfer.accounting.services;

import com.loulysoft.moneytransfer.accounting.enums.Criticite;
import com.loulysoft.moneytransfer.accounting.models.CreateTransactionRequest;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.models.TransactionRequest;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;

public interface TransactionService {

    TransactionReport finaliserTransaction(Long userId, Long companyId, Long reference);

    TransactionReport deroulerTransaction(Long userId, Long companyId, Long transactionId);

    <T extends Journal> Journal ecrireLignesComptables(
            Long transactionId, Long companyId, TransactionContext transactionContext, Class<T> journalClass);

    void deroulerService(Transaction transaction);

    TransactionReport exporterTransactionReport(Transaction transaction, TransactionReport report);

    TransactionReport createTransaction(CreateTransactionRequest request);

    TransactionTmp createTemporaryTransaction(TransactionRequest request);

    Transaction createTransaction(Transaction transaction);

    void deleteTemporaryTransactionWithTransactionId(Long transactionId);

    void createTransactionMouvementSolde(Transaction transaction, Criticite criticite, Character status);

    TransactionTmp updateTemporaryTransaction(TransactionTmp transactionTmp);

    Transaction updateTransaction(Transaction transaction);
}
