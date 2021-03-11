package com.epam.esm.repository.query;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.GiftCertificate_;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.Tag_;
import com.epam.esm.domain.entity.AbstractEntity_;
import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.query.util.QueryUtil;
import com.epam.esm.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GiftCertificateQueryBuilder {

    public CriteriaQuery<GiftCertificate> build(CriteriaBuilder criteriaBuilder, GiftCertificateCriteria criteria) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        Set<Predicate> predicates = new LinkedHashSet<>();

        String name = criteria.getName();
        if (name != null) {
            Predicate predicate = criteriaBuilder.like(
                    root.get(GiftCertificate_.name),
                    QueryUtil.anyMatchLikePattern(name));
            predicates.add(predicate);
        }

        String description = criteria.getDescription();
        if (description != null) {
            Predicate predicate = criteriaBuilder.like(
                    root.get(GiftCertificate_.description),
                    QueryUtil.anyMatchLikePattern(description));
            predicates.add(predicate);
        }

        String[] tagNames = criteria.getTagNames();
        if (tagNames != null) {
            Set<Predicate> tagPredicates = new HashSet<>();
            Join<GiftCertificate, Tag> tagJoin = root.join(GiftCertificate_.tags);
            for (String tagName : tagNames) {
                Predicate predicate = criteriaBuilder.equal(tagJoin.get(Tag_.name), tagName);
                tagPredicates.add(predicate);
            }
            Predicate tagPredicate = criteriaBuilder.or(tagPredicates.toArray(Predicate[]::new));
            predicates.add(tagPredicate);
        }

        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        criteriaQuery.select(root).where(finalPredicate).distinct(true);

        Sort sort = criteria.getSort();
        if (sort != null) {
            List<Order> orders = sort.getOrders().stream()
                    .map(order -> {
                        SingularAttribute<? super GiftCertificate, ?> singularAttribute;
                        if (order.getProperty().equalsIgnoreCase(GiftCertificate_.CREATE_DATE)) {
                            singularAttribute = GiftCertificate_.createDate;
                        } else if (order.getProperty().equalsIgnoreCase(GiftCertificate_.NAME)) {
                            singularAttribute = GiftCertificate_.name;
                        } else {
                            singularAttribute = AbstractEntity_.id;
                        }
                        return order.isAscending()
                                ? criteriaBuilder.asc(root.get(singularAttribute))
                                : criteriaBuilder.desc(root.get(singularAttribute));
                    }).collect(Collectors.toList());
            criteriaQuery.orderBy(orders);
        } else {
            Order defOrder = criteriaBuilder.asc(root.get(AbstractEntity_.id));
            criteriaQuery.orderBy(defOrder);
        }

        return criteriaQuery;
    }
}
