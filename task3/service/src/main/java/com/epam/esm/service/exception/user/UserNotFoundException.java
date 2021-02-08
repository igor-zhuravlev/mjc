package com.epam.esm.service.exception.user;

import com.epam.esm.service.exception.ServiceException;

public class UserNotFoundException extends ServiceException {
    private static final long serialVersionUID = -1434122984546858408L;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
