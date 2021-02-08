package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.criteria.Criteria;

import java.util.List;

public interface GiftCertificateRepository {
    List<GiftCertificate> findAll(Criteria criteria);
    GiftCertificate findById(Long id);
    GiftCertificate findByName(String name);
    GiftCertificate save(GiftCertificate giftCertificate);
    GiftCertificate update(GiftCertificate giftCertificate);
    void delete(GiftCertificate giftCertificate);
}
