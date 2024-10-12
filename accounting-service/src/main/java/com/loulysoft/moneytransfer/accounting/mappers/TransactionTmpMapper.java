package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.TransactionTmpEntity;
import com.loulysoft.moneytransfer.accounting.models.TransactionRequest;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface TransactionTmpMapper extends EntityMapper<TransactionTmp, TransactionTmpEntity> {

    @Mappings({@Mapping(target = "operations", ignore = true)})
    TransactionTmp toDto(TransactionTmpEntity entity);

    @InheritInverseConfiguration
    TransactionTmpEntity toEntity(TransactionTmp dto);

    TransactionTmpEntity toEntity(TransactionRequest request);

    default TransactionTmpEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionTmpEntity transaction = new TransactionTmpEntity();
        transaction.setId(id);
        return transaction;
    }
}
