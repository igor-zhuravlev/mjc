package com.epam.esm.repository.specification;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.GiftCertificate_;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.Tag_;
import com.epam.esm.repository.query.util.QueryUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Predicate;
import java.util.Arrays;

public interface GiftCertificateSpecification extends Specification<GiftCertificate> {

    static Specification<GiftCertificate> byPartOfName(String name) {
        return name == null ? null : (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(GiftCertificate_.name),
                        QueryUtil.anyMatchLikePattern(name));
    }

    static Specification<GiftCertificate> byPartOfDescription(String description) {
        return description == null ? null : (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(GiftCertificate_.description),
                        QueryUtil.anyMatchLikePattern(description));
    }

    static Specification<GiftCertificate> byTagNames(String[] tagNames) {
        return tagNames == null || tagNames.length == 0 ? null : (root, query, criteriaBuilder) -> {
            SetJoin<GiftCertificate, Tag> tagJoin = root.join(GiftCertificate_.tags);
            return criteriaBuilder.or(Arrays.stream(tagNames)
                    .map(tagName -> criteriaBuilder.equal(tagJoin.get(Tag_.name), tagName))
                    .toArray(Predicate[]::new));
        };
    }
}
