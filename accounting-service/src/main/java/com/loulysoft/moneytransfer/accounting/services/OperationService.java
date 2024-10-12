package com.loulysoft.moneytransfer.accounting.services;

import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.OperationRequest;
import com.loulysoft.moneytransfer.accounting.models.OperationTmp;

public interface OperationService {

    void createTemporaryOperation(OperationRequest request);

    void createOperation(Operation operation);

    OperationTmp findOperation(Long tempTransactionId, Long montantSchemaId);
}
