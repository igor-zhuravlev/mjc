package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.config.RepositoryTestConfig;
import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryTestConfig.class)
class GiftCertificateRepositoryImplTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    private List<GiftCertificate> giftCertificateList;

    @BeforeEach
    void beforeEach() {
        Map<CriteriaSearch, String> params = new HashMap<>();
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        giftCertificateList = giftCertificateRepository.findAll(criteria);

        assertNotEquals(0, giftCertificateList.size());
    }

    @Test
    void update_UpdatingCertificate_ReturnUpdatedRowCount() {
        Map<CriteriaSearch, String> params = new HashMap<>();
        params.put(CriteriaSearch.ID, String.valueOf(giftCertificateList.get(0).getId()));
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        GiftCertificate exited = giftCertificateRepository.find(criteria);

        exited.setName("newName");
        exited.setDuration(15);
        exited.setLastUpdateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));

        Long count = giftCertificateRepository.update(exited);

        assertEquals(1L, count);

        GiftCertificate updated = giftCertificateRepository.find(criteria);

        assertEquals(exited, updated);
    }

    @Test
    void findAll_FindingCertificates_ReturnCertificatesList() {
        Map<CriteriaSearch, String> params = new HashMap<>();
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        List<GiftCertificate> actual = giftCertificateRepository.findAll(criteria);

        assertEquals(actual.size(), giftCertificateList.size());
    }

    @Test
    void find_SearchForCertificate_ReturnFoundCertificate() {
        GiftCertificate giftCertificate = giftCertificateList.get(0);

        Map<CriteriaSearch, String> params = new HashMap<>();
        params.put(CriteriaSearch.ID, String.valueOf(giftCertificate.getId()));
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        GiftCertificate actual = giftCertificateRepository.find(criteria);

        assertEquals(giftCertificate, actual);
    }

    @Test
    void deleteById_DeletingCertificate_ReturnDeletedRowCount() {
        final Long id = giftCertificateList.get(0).getId();

        Long count = giftCertificateRepository.deleteById(id);

        assertEquals(1L, count);

        Map<CriteriaSearch, String> params = new HashMap<>();
        params.put(CriteriaSearch.ID, String.valueOf(id));
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        GiftCertificate actual = giftCertificateRepository.find(criteria);

        assertNull(actual);
    }

    @Test
    void save_SavingGiftCertificate_ReturnSavedCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("tag11");
        giftCertificate.setDescription("description1");
        giftCertificate.setPrice(BigDecimal.valueOf(9.99));
        giftCertificate.setDuration(10);
        giftCertificate.setCreateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));

        GiftCertificate saved = giftCertificateRepository.save(giftCertificate);

        Map<CriteriaSearch, String> params = new HashMap<>();
        params.put(CriteriaSearch.ID, String.valueOf(saved.getId()));
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        GiftCertificate actual = giftCertificateRepository.find(criteria);

        assertEquals(saved, actual);
    }
}