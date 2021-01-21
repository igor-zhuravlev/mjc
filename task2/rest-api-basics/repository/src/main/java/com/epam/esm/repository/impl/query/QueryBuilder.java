package com.epam.esm.repository.impl.query;

import com.epam.esm.repository.criteria.Criteria;

public interface QueryBuilder {
    String build(String query, Criteria criteria, String tablePrefix);
}
