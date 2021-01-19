package com.epam.esm.repository.util;

import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public final class QueryUtil {
    public static final String LIKE_PERCENT_SIGN = "%";

    public static final String SPACE_DELIMITER = " ";

    public static final String GIFT_CERTIFICATE_TABLE_PREFIX = "gc.";

    private static final String ORDER_BY_PREFIX = " ORDER BY ";
    private static final String ORDER_BY_PARAM_DELIMITER = ", ";

    public static String queryWithOrder(String query, Sort sort, String tablePrefix) {
        String orderPostfix = sort.stream()
                .map(order -> tablePrefix + order.getProperty() + SPACE_DELIMITER + order.getDirection())
                .collect(Collectors.joining(ORDER_BY_PARAM_DELIMITER, ORDER_BY_PREFIX, ""));
        return query + orderPostfix;
    }

    public static String buildQuery(String query, Criteria criteria, String tablePrefix) {
        Map<CriteriaSearch, String> params = criteria.getParams();
        if (params.size() == 0) {
            return criteria.getSort() != null
                    ? queryWithOrder(query, criteria.getSort(), GIFT_CERTIFICATE_TABLE_PREFIX)
                    : query;
        }

        StringJoiner stringJoiner = new StringJoiner(" AND ", " WHERE ", "");

        if (params.get(CriteriaSearch.ID) != null) {
            stringJoiner.add(tablePrefix + "id = " + params.get(CriteriaSearch.ID));
        } else if (params.get(CriteriaSearch.NAME) != null) {
            stringJoiner.add(tablePrefix + "name = '" + params.get(CriteriaSearch.NAME) + "'");
        }

        if (params.get(CriteriaSearch.TAG) != null) {
            stringJoiner.add("t.name = '" + params.get(CriteriaSearch.TAG) + "'");
        }
        if (params.get(CriteriaSearch.PART) != null) {
            String part = anyMatchLikePattern(params.get(CriteriaSearch.PART));
            stringJoiner.add("(gc.name LIKE '" + part + "' OR " + "gc.description LIKE '" + part + "')");
        }
        String newQuery = query + stringJoiner.toString();

        if (criteria.getSort() != null) {
            return queryWithOrder(newQuery, criteria.getSort(), GIFT_CERTIFICATE_TABLE_PREFIX);
        }

        return newQuery;
    }

    public static String anyMatchLikePattern(String value) {
        return LIKE_PERCENT_SIGN + value + LIKE_PERCENT_SIGN;
    }
}
