package com.epam.esm.repository.impl.query.impl;

import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.impl.query.QueryBuilder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderQueryBuilder extends AbstractQueryBuilder implements QueryBuilder {

    private static final String ORDER_BY_PREFIX = " ORDER BY ";

    private static final String SPACE_DELIMITER = " ";
    private static final String ORDER_BY_PARAM_DELIMITER = ", ";

    @Override
    public String build(String query, Criteria criteria, String tablePrefix) {
        String newQuery = query;

        if (criteria.getSort() != null) {
            String orderPostfix = criteria.getSort().stream()
                    .map(order -> tablePrefix + order.getProperty() + SPACE_DELIMITER + order.getDirection())
                    .collect(Collectors.joining(ORDER_BY_PARAM_DELIMITER, ORDER_BY_PREFIX, ""));
            newQuery += orderPostfix;
        }

        if (nextQueryBuilder != null) {
            return nextQueryBuilder.build(newQuery, criteria, tablePrefix);
        }

        return newQuery;
    }
}
