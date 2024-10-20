package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.TransactionEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldeEntity;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionMouvementSolde;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface TransactionMapper extends EntityMapper<Transaction, TransactionEntity> {

    // @Mappings({@Mapping(target = "operations", ignore = true)})
    Transaction toDto(TransactionEntity entity);

    TransactionEntity toEntity(Transaction dto);

    TransactionMouvementSolde toDto(TransactionMouvementSoldeEntity entity);

    TransactionMouvementSoldeEntity toEntity(TransactionMouvementSolde dto);

    default TransactionEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(id);
        return transaction;
    }
}
