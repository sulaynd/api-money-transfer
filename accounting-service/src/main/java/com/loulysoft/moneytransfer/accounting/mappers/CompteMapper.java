package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.CompteEntity;
import com.loulysoft.moneytransfer.accounting.models.Compte;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface CompteMapper extends EntityMapper<Compte, CompteEntity> {

    Compte toDto(CompteEntity entity);

    CompteEntity toEntity(Compte dto);

    default CompteEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        CompteEntity compteEntity = new CompteEntity();
        compteEntity.setId(id);
        return compteEntity;
    }
}
