package com.epam.esm.service.exception.validation;

import com.epam.esm.service.exception.ServiceException;

public class GiftCertificateParamsNotValidException extends ServiceException {
    private static final long serialVersionUID = -2871592337147686561L;

    public GiftCertificateParamsNotValidException(String message) {
        super(message);
    }
}
