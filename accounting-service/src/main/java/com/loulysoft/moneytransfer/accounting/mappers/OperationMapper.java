package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.OperationEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionEntity;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface OperationMapper {

    Operation toOperation(OperationEntity operationEntity);

    OperationEntity toOperationEntity(Operation operation);

    Transaction toTransaction(TransactionEntity transactionEntity);

    TransactionEntity toTransactionEntity(Transaction transaction);
}
