package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.entities.OperationEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionEntity;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.mappers.OperationMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionMapper;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.OperationRequest;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionRequest;
import com.loulysoft.moneytransfer.accounting.repositories.OperationRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionRepository;
import com.loulysoft.moneytransfer.accounting.services.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class OperationServiceImpl implements OperationService {

    private final OperationMapper operationMapper;
    private final OperationRepository operationRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(TransactionRequest request) {

        TransactionEntity newTransaction = TransactionMapper.convertToEntity(request);

        TransactionEntity savedTransaction = transactionRepository.save(newTransaction);

        return operationMapper.toTransaction(savedTransaction);
    }

    @Override
    public void createOperation(OperationRequest request) {
        OperationEntity newOperation = TransactionMapper.convertToEntity(request);

        OperationEntity savedOperation = operationRepository.save(newOperation);

        operationMapper.toOperation(savedOperation);
    }

    @Override
    @Transactional(readOnly = true)
    public Operation findOperation(Long tempTransactionId, Long montantSchemaId) {
        Operation operation = operationMapper.toOperation(operationRepository
                .findByTransactionIdAndMontantSchemaId(tempTransactionId, montantSchemaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operation with montantSchemaId " + montantSchemaId + " not found")));

        log.info("findOperation end ok - montantSchemaIdId: {}", montantSchemaId);
        log.trace("findOperation end ok - operation: {}", operation);

        return operation;
    }
}
