package com.epam.esm.repository.query;

import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface QueryBuilder<E> {
    CriteriaQuery<E> build(CriteriaBuilder criteriaBuilder, GiftCertificateCriteria criteria);
}
