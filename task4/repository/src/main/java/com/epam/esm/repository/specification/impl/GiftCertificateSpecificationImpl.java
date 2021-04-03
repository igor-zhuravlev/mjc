package com.epam.esm.repository.specification.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.specification.GiftCertificateSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GiftCertificateSpecificationImpl implements GiftCertificateSpecification {
    private static final long serialVersionUID = -7870482508990882975L;

    private final GiftCertificateCriteria criteria;

    public GiftCertificateSpecificationImpl(GiftCertificateCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return this
                .and(GiftCertificateSpecification.byPartOfName(criteria.getName()))
                .and(GiftCertificateSpecification.byPartOfDescription(criteria.getDescription()))
                .and(GiftCertificateSpecification.byTagNames(criteria.getTagNames()))
                .toPredicate(root, query, criteriaBuilder);
    }
}
