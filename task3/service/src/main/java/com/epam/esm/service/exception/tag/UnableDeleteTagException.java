package com.epam.esm.service.exception.tag;

import com.epam.esm.service.exception.ServiceException;

public class UnableDeleteTagException extends ServiceException {
    private static final long serialVersionUID = -295107923058807250L;

    public UnableDeleteTagException(String message) {
        super(message);
    }

    public UnableDeleteTagException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableDeleteTagException(Throwable cause) {
        super(cause);
    }
}
