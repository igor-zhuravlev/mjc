package com.epam.esm.repository.impl.query;

import com.epam.esm.repository.impl.query.impl.OrderQueryBuilder;
import com.epam.esm.repository.impl.query.impl.WhereQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class QueryBuilderProvider {

    private final QueryBuilder startQueryBuilder;

    @Autowired
    public QueryBuilderProvider(WhereQueryBuilder whereQueryBuilder, OrderQueryBuilder orderQueryBuilder) {
        whereQueryBuilder.setNextQueryBuilder(orderQueryBuilder);
        startQueryBuilder = whereQueryBuilder;
    }

    public QueryBuilder getQueryBuilder() {
        return startQueryBuilder;
    }
}
