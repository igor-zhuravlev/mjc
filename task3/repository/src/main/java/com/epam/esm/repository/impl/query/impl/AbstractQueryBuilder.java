package com.epam.esm.repository.impl.query.impl;

import com.epam.esm.repository.impl.query.QueryBuilder;

public abstract class AbstractQueryBuilder implements QueryBuilder {

    protected QueryBuilder nextQueryBuilder;

    public void setNextQueryBuilder(QueryBuilder queryBuilder) {
        nextQueryBuilder = queryBuilder;
    }
}
