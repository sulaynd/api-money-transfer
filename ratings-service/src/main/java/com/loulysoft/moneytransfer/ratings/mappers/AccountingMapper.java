package com.loulysoft.moneytransfer.ratings.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.ratings.entities.EcritureSchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.entities.MontantParamSchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantParamSchemaComptable;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
// @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AccountingMapper {
    List<MontantParamSchemaComptable> convertToMontantParamSchema(
            List<MontantParamSchemaComptableEntity> montantParamSchemaEntities);

    List<MontantParamSchemaComptableEntity> asMontantParamSchemaEntities(
            List<MontantParamSchemaComptable> montantParamSchemas);

    List<EcritureSchemaComptable> convertToEcritureSchemaComptable(
            List<EcritureSchemaComptableEntity> EcritureSchemaComptableEntities);

    List<EcritureSchemaComptableEntity> asEcritureSchemaComptableEntities(
            List<EcritureSchemaComptable> ecritureSchemas);
}
