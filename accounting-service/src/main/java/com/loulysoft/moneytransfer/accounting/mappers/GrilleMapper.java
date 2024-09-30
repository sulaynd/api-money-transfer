package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.GrilleEntity;
import com.loulysoft.moneytransfer.accounting.entities.GrilleItemEntity;
import com.loulysoft.moneytransfer.accounting.models.Grille;
import com.loulysoft.moneytransfer.accounting.models.GrilleItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface GrilleMapper {

    Grille toGrille(GrilleEntity grilleEntity);

    GrilleItem toGrilleItem(GrilleItemEntity grilleItemEntity);

    GrilleItemEntity toGrilleItemEntity(GrilleItem grilleItem);

    List<GrilleItem> toGrilleItems(List<GrilleItemEntity> grilleItemEntities);

    List<GrilleItemEntity> toGrilleItemEntities(List<GrilleItem> grilleItems);
}
