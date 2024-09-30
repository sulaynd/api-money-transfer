package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.entities.JournalEntity;
import com.loulysoft.moneytransfer.accounting.entities.OperationEntity;
import com.loulysoft.moneytransfer.accounting.entities.OperationTmpEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionTmpEntity;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.mappers.JournalMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationTmpMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransferMapper;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.OperationRequest;
import com.loulysoft.moneytransfer.accounting.models.OperationTmp;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionRequest;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import com.loulysoft.moneytransfer.accounting.repositories.JournalRepository;
import com.loulysoft.moneytransfer.accounting.repositories.OperationRepository;
import com.loulysoft.moneytransfer.accounting.repositories.OperationTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
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
    private final OperationTmpMapper tempMapper;
    private final JournalMapper journalMapper;
    private final TransactionTmpMapper transactionTmpMapper;
    private final TransactionMapper transactionMapper;
    private final OperationTmpRepository operationTmpRepository;
    private final OperationRepository operationRepository;
    private final TransactionTmpRepository transactionTmpRepository;
    private final TransactionRepository transactionRepository;
    private final JournalRepository journalRepository;

    @Override
    public TransactionTmp createTemporaryTransaction(TransactionRequest request) {

        TransactionTmpEntity newTransaction = TransferMapper.convertToEntity(request);

        TransactionTmpEntity savedTransaction = transactionTmpRepository.save(newTransaction);

        return transactionTmpMapper.toDto(savedTransaction);
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        TransactionEntity newTransaction = transactionMapper.toEntity(transaction);

        TransactionEntity savedTransaction = transactionRepository.save(newTransaction);

        return transactionMapper.toDto(savedTransaction);
    }

    @Override
    public void createTemporaryOperation(OperationRequest request) {
        OperationTmpEntity newOperation = TransferMapper.convertToEntity(request);

        OperationTmpEntity savedOperation = operationTmpRepository.save(newOperation);

        tempMapper.toDto(savedOperation);
    }

    @Override
    public void createOperation(Operation operation) {
        OperationEntity newOperation = operationMapper.toEntity(operation);

        OperationEntity savedOperation = operationRepository.save(newOperation);

        operationMapper.toDto(savedOperation);
    }

    @Override
    public Journal createJournal(Journal journal) {

        JournalEntity newJournal = journalMapper.toEntity(journal);

        JournalEntity savedJournal = journalRepository.save(newJournal);

        return journalMapper.toDto(savedJournal);
    }

    @Override
    @Transactional(readOnly = true)
    public OperationTmp findOperation(Long tempTransactionId, Long montantSchemaId) {
        OperationTmp operation = tempMapper.toDto(operationTmpRepository
                .findByTransactionIdAndMontantSchemaId(tempTransactionId, montantSchemaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operation with montantSchemaId " + montantSchemaId + " not found")));

        log.info("findOperation end ok - montantSchemaIdId: {}", montantSchemaId);
        log.trace("findOperation end ok - operation: {}", operation);

        return operation;
    }
}
