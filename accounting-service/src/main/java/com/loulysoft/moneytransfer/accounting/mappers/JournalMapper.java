package com.loulysoft.moneytransfer.accounting.mappers;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.loulysoft.moneytransfer.accounting.entities.JournalEntity;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface JournalMapper extends EntityMapper<Journal, JournalEntity> {

    Journal toDto(JournalEntity entity);

    JournalEntity toEntity(Journal dto);

    default JournalEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        JournalEntity journalEntity = new JournalEntity();
        journalEntity.setId(id);
        return journalEntity;
    }
}
