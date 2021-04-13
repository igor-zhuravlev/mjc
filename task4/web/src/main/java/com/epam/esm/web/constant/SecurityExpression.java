package com.epam.esm.web.constant;

public final class SecurityExpression {
    public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String HAS_ROLE_USER = "hasRole('USER')";

    private SecurityExpression() {
    }
}
