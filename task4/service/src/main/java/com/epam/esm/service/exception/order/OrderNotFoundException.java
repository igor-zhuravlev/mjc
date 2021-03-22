package com.epam.esm.service.exception.order;

import com.epam.esm.service.exception.ServiceException;

public class OrderNotFoundException extends ServiceException {
    private static final long serialVersionUID = 7838067838831623868L;

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Throwable cause) {
        super(cause);
    }
}
