package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.OperationEntity;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface OperationMapper extends EntityMapper<Operation, OperationEntity> {

    Operation toDto(OperationEntity entity);

    OperationEntity toEntity(Operation dto);

    default OperationEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        OperationEntity operation = new OperationEntity();
        operation.setId(id);
        return operation;
    }
}
