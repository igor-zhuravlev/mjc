package com.epam.esm.repository.impl;

import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.User;
import com.epam.esm.domain.entity.OrderGiftCertificate;
import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.Tag_;
import com.epam.esm.domain.entity.OrderGiftCertificate_;
import com.epam.esm.domain.entity.GiftCertificate_;
import com.epam.esm.domain.entity.Order_;
import com.epam.esm.domain.entity.Order;
import com.epam.esm.repository.TagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Join;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TagRepositoryImpl implements TagRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> findAll(int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        criteriaQuery.select(root);

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Tag findById(Long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(Tag_.name), name));

        return entityManager.createQuery(criteriaQuery).getResultStream()
                .findFirst().orElse(null);
    }

    @Override
    public Tag findMostWidelyUsedTagWithHighestCostOfOrdersByUser(User user) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        SetJoin<Order, OrderGiftCertificate> orderGiftCertificateJoin =
                root.join(Order_.orderGiftCertificates);
        Join<OrderGiftCertificate, GiftCertificate> orderGiftCertificateGiftCertificateJoin =
                orderGiftCertificateJoin.join(OrderGiftCertificate_.giftCertificate);
        SetJoin<GiftCertificate, Tag> giftCertificateTagJoin =
                orderGiftCertificateGiftCertificateJoin.join(GiftCertificate_.tags);

        Expression<Long> tagCount = criteriaBuilder.count(giftCertificateTagJoin.get(Tag_.name));
        Expression<BigDecimal> maxAmount = criteriaBuilder.max(root.get(Order_.amount));

        Path<Long> tagId = giftCertificateTagJoin.get(Tag_.id);
        Path<String> tagName = giftCertificateTagJoin.get(Tag_.name);

        criteriaQuery.multiselect(tagId, tagName, tagCount, maxAmount);
        criteriaQuery.where(criteriaBuilder.equal(root.get(Order_.user), user));
        criteriaQuery.groupBy(tagId);
        criteriaQuery.orderBy(criteriaBuilder.desc(tagCount), criteriaBuilder.desc(maxAmount));

        List<Tuple> tagTupleList = entityManager.createQuery(criteriaQuery)
                .setMaxResults(1)
                .getResultStream()
                .collect(Collectors.toList());

        return tagTupleList.stream()
                .map(tuple -> {
                    Tag tag = new Tag();
                    tag.setId(tuple.get(giftCertificateTagJoin.get(Tag_.ID)));
                    tag.setName(tuple.get(giftCertificateTagJoin.get(Tag_.NAME)));
                    return tag;
                }).findFirst().orElse(null);
    }

    @Override
    public Tag save(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }
}
