package com.epam.esm.repository;

import java.util.List;

public interface CrudRepository<E, ID> {
    List<E> findAll(int offset, int limit);
    E findById(ID id);
    E save(E entity);
    void delete(E entity);
}
