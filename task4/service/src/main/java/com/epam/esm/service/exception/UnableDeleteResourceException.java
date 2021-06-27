package com.epam.esm.service.exception;

public class UnableDeleteResourceException extends ServiceException {
    private static final long serialVersionUID = 2470705581585187848L;

    public UnableDeleteResourceException(String message) {
        super(message);
    }

    public UnableDeleteResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableDeleteResourceException(Throwable cause) {
        super(cause);
    }
}
