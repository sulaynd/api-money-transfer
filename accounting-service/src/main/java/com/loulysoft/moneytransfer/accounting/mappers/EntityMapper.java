package com.loulysoft.moneytransfer.accounting.mappers;

import java.util.List;

public interface EntityMapper<D, E> {

    public E toEntity(D dto);

    public D toDto(E entity);

    public List<E> toEntityList(List<D> dtoList);

    public List<D> toDtoList(List<E> entityList);

    // public Set<E> toEntitySet(Set<D> dtoSet);
    // public Set <D> toDtoSet(Set<E> entitySet);
}
