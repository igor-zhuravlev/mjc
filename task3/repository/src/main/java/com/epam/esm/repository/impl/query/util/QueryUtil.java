package com.epam.esm.repository.impl.query.util;

public final class QueryUtil {
    public static final String LIKE_PERCENT_SIGN = "%";

    public static String anyMatchLikePattern(String value) {
        return LIKE_PERCENT_SIGN + value + LIKE_PERCENT_SIGN;
    }
}
