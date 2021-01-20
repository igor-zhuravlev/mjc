package com.epam.esm.service.exception.validation;

import com.epam.esm.service.exception.ServiceException;

public class TagNotValidException extends ServiceException {
    private static final long serialVersionUID = 6907882047316364070L;

    public TagNotValidException(String message) {
        super(message);
    }
}
