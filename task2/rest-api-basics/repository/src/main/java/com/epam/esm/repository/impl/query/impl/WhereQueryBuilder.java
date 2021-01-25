package com.epam.esm.repository.impl.query.impl;

import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import com.epam.esm.repository.impl.query.QueryBuilder;
import com.epam.esm.repository.impl.query.util.QueryUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.StringJoiner;

@Component
public class WhereQueryBuilder extends AbstractQueryBuilder implements QueryBuilder {

    private static final String WHERE_PREFIX = " WHERE ";
    private static final String AND_DELIMITER = " AND ";

    @Override
    public String build(String query, Criteria criteria, String tablePrefix) {
        Map<CriteriaSearch, String> params = criteria.getParams();

        if (params.size() == 0) {
            return nextQueryBuilder != null
                    ? nextQueryBuilder.build(query, criteria, tablePrefix)
                    : query;
        }

        StringJoiner stringJoiner = new StringJoiner(AND_DELIMITER, WHERE_PREFIX, "");

        if (params.get(CriteriaSearch.ID) != null) {
            stringJoiner.add(tablePrefix + "id = " + params.get(CriteriaSearch.ID));
        } else if (params.get(CriteriaSearch.NAME) != null) {
            stringJoiner.add(tablePrefix + "name = '" + params.get(CriteriaSearch.NAME) + "'");
        }

        if (params.get(CriteriaSearch.TAG) != null) {
            stringJoiner.add("t.name = '" + params.get(CriteriaSearch.TAG) + "'");
        }
        if (params.get(CriteriaSearch.PART) != null) {
            String part = QueryUtil.anyMatchLikePattern(params.get(CriteriaSearch.PART));
            stringJoiner.add("(gc.name LIKE '" + part + "' OR " + "gc.description LIKE '" + part + "')");
        }

        String newQuery = query + stringJoiner.toString();

        if (nextQueryBuilder != null) {
            return nextQueryBuilder.build(newQuery, criteria, tablePrefix);
        }

        return newQuery;
    }
}
