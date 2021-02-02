package com.epam.esm.service.validation;

public abstract class Validator<T> {
    public abstract boolean isValidToSave(T object);

    public boolean isValidToUpdate(T object) {
        return false;
    }
}
