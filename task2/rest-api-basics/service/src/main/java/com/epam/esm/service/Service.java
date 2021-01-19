package com.epam.esm.service;

import com.epam.esm.service.exception.ServiceException;

public interface Service<E, V> {
    E findById(V id) throws ServiceException;
    E save(E dto) throws ServiceException;
    void deleteById(V id) throws ServiceException;
}
