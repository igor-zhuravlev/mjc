package com.epam.esm.repository.specification.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.specification.GiftCertificateSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

public class GiftCertificateSpecificationImpl implements GiftCertificateSpecification {
    private static final long serialVersionUID = -7870482508990882975L;

    private final GiftCertificateCriteria criteria;

    public GiftCertificateSpecificationImpl(GiftCertificateCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Set<Predicate> predicates = new HashSet<>();
        if (criteria.getName() != null) {
            Predicate predicateName = GiftCertificateSpecification.byPartOfName(criteria.getName())
                    .toPredicate(root, query, criteriaBuilder);
            predicates.add(predicateName);
        }
        if (criteria.getDescription() != null) {
            Predicate predicateDescription = GiftCertificateSpecification
                    .byPartOfDescription(criteria.getDescription())
                    .toPredicate(root, query, criteriaBuilder);
            predicates.add(predicateDescription);
        }
        if (criteria.getTagNames() != null) {
            Predicate predicateTags = GiftCertificateSpecification.byTagNames(criteria.getTagNames())
                    .toPredicate(root, query, criteriaBuilder);
            predicates.add(predicateTags);
        }
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
