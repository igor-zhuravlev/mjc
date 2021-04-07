package com.epam.esm.repository.impl;

import com.epam.esm.domain.entity.AbstractEntity;
import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.config.RepositoryTestConfig;
import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
class GiftCertificateRepositoryImplTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    private List<GiftCertificate> giftCertificateList;

    private static int offset;
    private static int limit;

    @BeforeAll
    static void beforeAll() {
        offset = 0;
        limit = 5;
    }

    @BeforeEach
    void beforeEach() {
        giftCertificateList = giftCertificateRepository.findAll(offset, limit);
        assertNotEquals(0, giftCertificateList.size());
    }

    @Test
    void findAll_SearchForCertificatesWithOffsetAndLimit_ReturnCertificateList() {
        List<GiftCertificate> actual = giftCertificateRepository.findAll(offset, limit);
        assertEquals(giftCertificateList, actual);
    }

    @Test
    void findAll_SearchForCertificatesByCriteriaWithOffsetAndLimit_ReturnCertificateList() {
        GiftCertificateCriteria giftCertificateCriteria = new GiftCertificateCriteria();

        List<GiftCertificate> actual = giftCertificateRepository
                .findAll(giftCertificateCriteria, offset, limit);

        List<GiftCertificate> expected = giftCertificateList.stream()
                .sorted(Comparator.comparingLong(AbstractEntity::getId))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    void findById_SearchForCertificateById_ReturnFoundCertificate() {
        GiftCertificate existedGiftCertificate = giftCertificateList.get(0);
        final Long id = existedGiftCertificate.getId();

        GiftCertificate actual = giftCertificateRepository.findById(id);

        assertEquals(existedGiftCertificate, actual);
    }

    @Test
    void findByName_SearchForCertificateByName_ReturnFoundCertificate() {
        GiftCertificate existedGiftCertificate = giftCertificateList.get(0);
        final String name = existedGiftCertificate.getName();

        GiftCertificate actual = giftCertificateRepository.findByName(name);

        assertEquals(existedGiftCertificate, actual);
    }

    @Test
    void save_SavingCertificate_ReturnSavedCertificate() {
        GiftCertificate giftCertificateToPersist = new GiftCertificate();
        giftCertificateToPersist.setName("new_gift_certificate");
        giftCertificateToPersist.setDescription("description");
        giftCertificateToPersist.setPrice(BigDecimal.valueOf(9.99));
        giftCertificateToPersist.setDuration(10);
        giftCertificateToPersist.setCreateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));
        giftCertificateToPersist.setLastUpdateDate(giftCertificateToPersist.getCreateDate());

        GiftCertificate persistedGiftCertificate = giftCertificateRepository
                .save(giftCertificateToPersist);
        Long persistedGiftCertificateId = persistedGiftCertificate.getId();

        assertNotNull(persistedGiftCertificate);

        GiftCertificate actual = giftCertificateRepository
                .findById(persistedGiftCertificateId);

        assertEquals(persistedGiftCertificate, actual);
    }

    @Test
    void update_UpdatingCertificate_ReturnUpdatedCertificate() {
        GiftCertificate existedGiftCertificate = giftCertificateList.get(0);
        final Long existedGiftCertificateId = existedGiftCertificate.getId();

        GiftCertificate giftCertificateToUpdate = giftCertificateRepository
                .findById(existedGiftCertificateId);

        giftCertificateToUpdate.setDuration(25);
        giftCertificateToUpdate.setLastUpdateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));

        GiftCertificate updatedGiftCertificate = giftCertificateRepository
                .update(giftCertificateToUpdate);

        assertNotNull(updatedGiftCertificate);

        GiftCertificate actual = giftCertificateRepository
                .findById(existedGiftCertificateId);

        assertEquals(updatedGiftCertificate, actual);
    }

    @Test
    void delete_DeletingCertificate_ReturnNothing() {
        Long giftCertificateId = giftCertificateList.get(0).getId();

        GiftCertificate existedGiftCertificate = giftCertificateRepository
                .findById(giftCertificateId);

        assertNotNull(existedGiftCertificate);

        giftCertificateRepository.delete(existedGiftCertificate);

        GiftCertificate actual = giftCertificateRepository
                .findById(giftCertificateId);

        assertNull(actual);
    }
}
