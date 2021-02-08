package com.epam.esm.service;

public interface Service<E, V> {
    E findById(V id);
    E save(E dto);
    void deleteById(V id);
}
