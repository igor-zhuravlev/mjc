package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;

import java.util.List;

public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {
    List<GiftCertificate> findAll(GiftCertificateCriteria criteria, int offset, int limit);
    GiftCertificate findByName(String name);
    GiftCertificate update(GiftCertificate giftCertificate);
}
