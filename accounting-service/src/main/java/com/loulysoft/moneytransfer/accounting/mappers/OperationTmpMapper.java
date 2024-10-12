package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.OperationTmpEntity;
import com.loulysoft.moneytransfer.accounting.models.OperationRequest;
import com.loulysoft.moneytransfer.accounting.models.OperationTmp;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface OperationTmpMapper extends EntityMapper<OperationTmp, OperationTmpEntity> {

    @Mappings({@Mapping(target = "transaction.operations", ignore = true)})
    OperationTmp toDto(OperationTmpEntity entity);

    @AfterMapping
    default void ignoreTransactionOperations(OperationTmpEntity op, @MappingTarget OperationTmp opDto) {
        opDto.getTransaction().setOperations(null);
    }

    @InheritInverseConfiguration
    OperationTmpEntity toEntity(OperationTmp dto);

    OperationTmpEntity toEntity(OperationRequest request);

    default OperationTmpEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        OperationTmpEntity operationTmp = new OperationTmpEntity();
        operationTmp.setId(id);
        return operationTmp;
    }
}
