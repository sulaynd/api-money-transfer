package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.UniteOrganisationalEntity;
import com.loulysoft.moneytransfer.accounting.entities.ZoneMonetaireEntity;
import com.loulysoft.moneytransfer.accounting.models.PaysRecord;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.models.ZoneMonetaire;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface CompanyMapper {

    UniteOrganisational toUniteOrganisational(UniteOrganisationalEntity uniteOrganisationalEntity);

    PaysRecord toPaysRecord(PaysRecord uniteOrganisationalEntity);

    UniteOrganisationalEntity toUniteOrganisationalEntity(UniteOrganisational uniteOrganisational);

    ZoneMonetaire toZoneMonetaire(ZoneMonetaireEntity zoneMonetaireEntity);

    ZoneMonetaireEntity toZoneMonetaireEntity(ZoneMonetaire zoneMonetaire);
}
