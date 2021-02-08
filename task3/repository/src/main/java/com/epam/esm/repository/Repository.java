package com.epam.esm.repository;

import com.epam.esm.repository.criteria.Criteria;

public interface Repository<E, T> {
    E save(E entity);
    E find(Criteria criteria);
    T deleteById(T id);
}
