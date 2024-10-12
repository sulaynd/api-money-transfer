package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.entities.OperationEntity;
import com.loulysoft.moneytransfer.accounting.entities.OperationTmpEntity;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.OperationMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationTmpMapper;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.OperationRequest;
import com.loulysoft.moneytransfer.accounting.models.OperationTmp;
import com.loulysoft.moneytransfer.accounting.repositories.OperationRepository;
import com.loulysoft.moneytransfer.accounting.repositories.OperationTmpRepository;
import com.loulysoft.moneytransfer.accounting.services.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OperationServiceImpl implements OperationService {

    private final OperationMapper operationMapper;

    private final OperationTmpMapper opTmpMapper;

    private final OperationTmpRepository operationTmpRepository;

    private final OperationRepository operationRepository;

    @Override
    public void createTemporaryOperation(OperationRequest request) {
        try {
            OperationTmpEntity newOperation = opTmpMapper.toEntity(request);

            OperationTmpEntity savedOperation = operationTmpRepository.save(newOperation);

            opTmpMapper.toDto(savedOperation);

        } catch (RuntimeException e) {
            throw new TransactionException("Error while creating temporary operation : " + e.getMessage());
        }
    }

    @Override
    public void createOperation(Operation operation) {
        try {
            OperationEntity newOperation = operationMapper.toEntity(operation);

            OperationEntity savedOperation = operationRepository.save(newOperation);

            operationMapper.toDto(savedOperation);

        } catch (RuntimeException e) {
            throw new TransactionException("Error while creating operation : " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OperationTmp findOperation(Long tempTransactionId, Long montantSchemaId) {
        OperationTmp operation = opTmpMapper.toDto(operationTmpRepository
                .findByTransactionIdAndMontantSchemaId(tempTransactionId, montantSchemaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operation with montantSchemaId " + montantSchemaId + " not found")));

        log.info("findOperation end ok - montantSchemaIdId: {}", montantSchemaId);
        log.trace("findOperation end ok - operation: {}", operation);

        return operation;
    }
}
