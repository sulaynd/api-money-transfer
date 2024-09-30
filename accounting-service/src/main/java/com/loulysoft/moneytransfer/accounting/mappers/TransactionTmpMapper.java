package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.TransactionTmpEntity;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface TransactionTmpMapper extends EntityMapper<TransactionTmp, TransactionTmpEntity> {

    TransactionTmp toDto(TransactionTmpEntity transactionTmpEntity);

    TransactionTmpEntity toEntity(TransactionTmp transactionTmp);

    default TransactionTmpEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionTmpEntity transaction = new TransactionTmpEntity();
        transaction.setId(id);
        return transaction;
    }
}
