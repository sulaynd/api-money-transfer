package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.CalculMontantSchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.entities.EcritureSchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.entities.GrilleEntity;
import com.loulysoft.moneytransfer.accounting.entities.GrilleItemEntity;
import com.loulysoft.moneytransfer.accounting.entities.MontantParamSchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.entities.MontantSchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.entities.ParametreRechercheEntity;
import com.loulysoft.moneytransfer.accounting.entities.SchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.entities.TypeServiceEntity;
import com.loulysoft.moneytransfer.accounting.entities.ValeurParametreEntity;
import com.loulysoft.moneytransfer.accounting.models.CalculMontantSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Grille;
import com.loulysoft.moneytransfer.accounting.models.GrilleItem;
import com.loulysoft.moneytransfer.accounting.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.ParametreRecherche;
import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.TypeService;
import com.loulysoft.moneytransfer.accounting.models.ValeurParametre;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
// @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AccountingMapper {

    SchemaComptable toSchema(SchemaComptableEntity schemaEntity);

    MontantSchemaComptable toMontantSchema(MontantSchemaComptableEntity montantSchemaEntity);

    SchemaComptableEntity toSchemaEntity(SchemaComptable schema);

    MontantSchemaComptableEntity toMontantSchemaEntity(MontantSchemaComptable montantSchema);

    MontantParamSchemaComptable toMontantParamSchema(MontantParamSchemaComptableEntity montantParamSchemaEntity);

    EcritureSchemaComptable toEcritureSchema(EcritureSchemaComptableEntity ecritureSchemaEntity);

    EcritureSchemaComptableEntity toEcritureSchemaEntity(EcritureSchemaComptable ecritureSchema);

    ValeurParametre toValeurParametre(ValeurParametreEntity valeurParametreEntity);

    Grille toGrille(GrilleEntity grilleEntity);

    GrilleEntity toGrilleEntity(Grille grille);

    GrilleItem toGrilleItem(GrilleItemEntity grilleItemEntity);

    GrilleItemEntity toGrilleItemEntity(GrilleItem grilleItem);

    ParametreRecherche toParametreRecherche(ParametreRechercheEntity parametreRecherche);

    ParametreRechercheEntity toParametreRechercheEntity(GrilleItem grilleItem);

    TypeService toTypeService(TypeServiceEntity typeServiceEntity);

    List<MontantParamSchemaComptable> convertToMontantParamSchema(
            List<MontantParamSchemaComptableEntity> montantParamSchemaEntities);

    List<MontantParamSchemaComptableEntity> toMontantParamSchemaEntities(
            List<MontantParamSchemaComptable> montantParamSchemas);

    List<EcritureSchemaComptable> convertToEcritureSchema(
            List<EcritureSchemaComptableEntity> EcritureSchemaComptableEntities);

    List<EcritureSchemaComptableEntity> toEcritureSchemaEntities(List<EcritureSchemaComptable> ecritureSchemas);

    List<MontantSchemaComptable> convertToMontantSchema(List<MontantSchemaComptableEntity> montantSchemaEntities);

    List<MontantSchemaComptableEntity> toMontantSchemaEntities(List<MontantSchemaComptable> montantSchemas);

    List<CalculMontantSchemaComptable> convertToCalculMontantSchema(
            List<CalculMontantSchemaComptableEntity> calculMontantSchemaEntities);

    List<CalculMontantSchemaComptableEntity> toCalculMontantSchemaEntities(
            List<CalculMontantSchemaComptable> calculMontantSchemas);

    List<GrilleItem> convertToGrilleItem(List<GrilleItemEntity> grilleItemEntities);

    List<GrilleItemEntity> toGrilleItemEntities(List<GrilleItem> grilleItems);
}
