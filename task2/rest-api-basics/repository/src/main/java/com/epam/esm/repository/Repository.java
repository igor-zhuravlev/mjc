package com.epam.esm.repository;

import com.epam.esm.repository.exception.RepositoryException;

public interface Repository<E, T> {
    E save(E entity) throws RepositoryException;
    E findById(T id) throws RepositoryException;
    E findByName(String name) throws RepositoryException;
    T deleteById(T id) throws RepositoryException;
}
