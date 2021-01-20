package com.epam.esm.service.exception.validation;

import com.epam.esm.service.exception.ServiceException;

public class GiftCertificateNotValidException extends ServiceException {
    private static final long serialVersionUID = 565105885513794787L;

    public GiftCertificateNotValidException(String message) {
        super(message);
    }
}
