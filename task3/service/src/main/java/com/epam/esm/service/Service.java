package com.epam.esm.service;

public interface Service<D, ID> {
    D findById(ID id);
}
