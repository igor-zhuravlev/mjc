package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.util.Criteria;

import java.util.List;

public interface GiftCertificateRepository extends Repository<GiftCertificate, Long> {
    List<GiftCertificate> findAll(Criteria criteria) throws RepositoryException;
    Long update(GiftCertificate giftCertificate) throws RepositoryException;
}
