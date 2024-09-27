package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.CoursDeviseEntity;
import com.loulysoft.moneytransfer.accounting.entities.DeviseEntity;
import com.loulysoft.moneytransfer.accounting.entities.ParametreRechercheEntity;
import com.loulysoft.moneytransfer.accounting.entities.PaysEntity;
import com.loulysoft.moneytransfer.accounting.models.CoursDevise;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.ParametreRecherche;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
// @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DeviseMapper {

    Devise toDevise(DeviseEntity deviseEntity);

    ParametreRecherche toParametreRecherche(ParametreRechercheEntity parametreRechercheEntity);

    ParametreRechercheEntity toParametreRechercheEntity(ParametreRecherche parametreRecherche);

    Pays toPays(PaysEntity paysEntity);

    PaysEntity toPaysEntity(Pays pays);

    CoursDevise toCoursDevise(CoursDeviseEntity coursDeviseEntity);

    CoursDeviseEntity toCoursDeviseEntity(CoursDevise coursDevise);
}
