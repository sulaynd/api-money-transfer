package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.OperationTmpEntity;
import com.loulysoft.moneytransfer.accounting.models.OperationTmp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface OperationTmpMapper extends EntityMapper<OperationTmp, OperationTmpEntity> {

    OperationTmp toDto(OperationTmpEntity entity);

    OperationTmpEntity toEntity(OperationTmp tdo);

    default OperationTmpEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        OperationTmpEntity operationTmp = new OperationTmpEntity();
        operationTmp.setId(id);
        return operationTmp;
    }
}
