package com.epam.esm.service.converter;

import java.util.List;

public interface Converter<E, D> {
    E dtoToEntity(D dto);
    D entityToDto(E entity);
    List<E> dtoToEntityList(List<D> dtoList);
    List<D> entityToDtoList(List<E> entityList);
}
