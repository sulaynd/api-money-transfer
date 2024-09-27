package com.loulysoft.moneytransfer.accounting.services;

import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.OperationRequest;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionRequest;

public interface OperationService {

    Transaction createTransaction(TransactionRequest request);

    void createOperation(OperationRequest request);

    Operation findOperation(Long tempTransactionId, Long montantSchemaId);
}
