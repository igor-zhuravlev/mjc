package com.epam.esm.service.exception;

public class ResourceAlreadyExistException extends ServiceException {
    private static final long serialVersionUID = -501204698900409737L;

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
