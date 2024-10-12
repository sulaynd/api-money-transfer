package com.loulysoft.moneytransfer.accounting.mappers;

import java.util.List;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<D> toDtoList(List<E> entityList);

    // List<E> toEntityList(List<D> dtoList);

    // Set<E> toEntitySet(Set<D> dtoSet);
    // Set<D> toDtoSet(Set<E> entitySet);
}
