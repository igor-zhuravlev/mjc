package com.epam.esm.service.constant;

public enum ServiceError {
    TAG_NOT_FOUND("1001"),
    TAG_ALREADY_EXISTS("1002"),
    TAG_NOT_VALID("1011"),

    GIFT_CERTIFICATE_NOT_FOUNT("2001"),
    GIFT_CERTIFICATE_ALREADY_EXISTS("2002"),
    GIFT_CERTIFICATE_NOT_VALID("2011"),
    GIFT_CERTIFICATE_PARAMS_NOT_VALID("2012"),

    USER_NOT_FOUND("3001"),

    INTERNAL_SERVER_ERROR("0000");

    private final String code;

    ServiceError(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
