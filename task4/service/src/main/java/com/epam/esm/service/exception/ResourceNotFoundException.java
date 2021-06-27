package com.epam.esm.service.exception;

public class ResourceNotFoundException extends ServiceException{
    private static final long serialVersionUID = 5180420828113813986L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
