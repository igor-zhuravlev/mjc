package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificate_;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.query.QueryBuilder;
import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private QueryBuilder<GiftCertificate> giftCertificateQueryBuilder;

    @Override
    public List<GiftCertificate> findAll(GiftCertificateCriteria criteria, int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<GiftCertificate> criteriaQuery = giftCertificateQueryBuilder
                .build(criteriaBuilder, criteria);

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findAll(int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        criteriaQuery.select(root);

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public GiftCertificate findById(Long id) {
        return entityManager.find(GiftCertificate.class, id);
    }

    @Override
    public GiftCertificate findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(GiftCertificate_.name), name));

        return entityManager.createQuery(criteriaQuery).getResultStream()
                .findFirst().orElse(null);
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        return giftCertificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        GiftCertificate merged = entityManager.merge(giftCertificate);
        entityManager.flush();
        entityManager.refresh(merged);
        return merged;
    }

    @Override
    public void delete(GiftCertificate giftCertificate) {
        entityManager.remove(giftCertificate);
    }
}
