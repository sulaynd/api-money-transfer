package com.loulysoft.moneytransfer.accounting.services;

import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.OperationRequest;
import com.loulysoft.moneytransfer.accounting.models.OperationTmp;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionRequest;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;

public interface OperationService {

    TransactionTmp createTemporaryTransaction(TransactionRequest request);

    Transaction createTransaction(Transaction transaction);

    void createTemporaryOperation(OperationRequest request);

    void createOperation(Operation operation);

    Journal createJournal(Journal journal);

    OperationTmp findOperation(Long tempTransactionId, Long montantSchemaId);
}
