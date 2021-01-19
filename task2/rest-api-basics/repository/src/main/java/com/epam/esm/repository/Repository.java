package com.epam.esm.repository;

import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.criteria.Criteria;

public interface Repository<E, T> {
    E save(E entity) throws RepositoryException;
    E find(Criteria criteria) throws RepositoryException;
    T deleteById(T id) throws RepositoryException;
}
