package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.exception.RepositoryException;

import java.util.List;
import java.util.Set;

public interface GiftCertificateRepository extends Repository<GiftCertificate, Long> {
    List<GiftCertificate> findAll(Criteria criteria) throws RepositoryException;
    Long update(GiftCertificate giftCertificate) throws RepositoryException;
    void bindGiftCertificateWithTags(Long id, Set<Tag> tags) throws RepositoryException;
}
