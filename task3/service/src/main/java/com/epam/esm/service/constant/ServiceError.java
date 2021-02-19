package com.epam.esm.service.constant;

public enum ServiceError {
    TAG_NOT_FOUND("1001"),
    TAG_ALREADY_EXISTS("1002"),

    GIFT_CERTIFICATE_NOT_FOUNT("2001"),
    GIFT_CERTIFICATE_ALREADY_EXISTS("2002"),

    USER_NOT_FOUND("3001"),

    ORDER_NOT_FOUND("4001"),

    INTERNAL_SERVER_ERROR("0000"),

    INVALID_ARGUMENTS("0001"),
    INVALID_ARGUMENT_TYPE("0011");

    private final String code;

    ServiceError(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
