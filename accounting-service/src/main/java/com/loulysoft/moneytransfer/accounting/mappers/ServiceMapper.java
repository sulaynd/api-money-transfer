package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.CompteEntity;
import com.loulysoft.moneytransfer.accounting.entities.NatureServiceEntity;
import com.loulysoft.moneytransfer.accounting.models.Compte;
import com.loulysoft.moneytransfer.accounting.models.NatureService;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface ServiceMapper {

    NatureService toNatureService(NatureServiceEntity natureServiceEntity);

    NatureServiceEntity toNatureServiceEntity(NatureService natureService);

    Compte toCompte(CompteEntity compteEntity);

    CompteEntity toCompteEntity(Compte compte);
}
