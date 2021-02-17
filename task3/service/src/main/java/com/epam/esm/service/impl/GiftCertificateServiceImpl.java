package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.exception.certificate.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.certificate.GiftCertificateNotFoundException;
import com.epam.esm.service.util.GiftCertificateCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private Converter<GiftCertificate, GiftCertificateDto> giftCertificateConverter;

    @Transactional(readOnly = true)
    @Override
    public List<GiftCertificateDto> findAll(GiftCertificateParamDto giftCertificateParam, PageDto pageDto) {
        GiftCertificateCriteria criteria = GiftCertificateCriteriaBuilder.build(giftCertificateParam);
        System.out.println(criteria);
        List<GiftCertificate> giftCertificateList = giftCertificateRepository
                .findAll(criteria, pageDto.getOffset(), pageDto.getLimit());
        return giftCertificateConverter.entityToDtoList(giftCertificateList);
    }

    @Transactional(readOnly = true)
    @Override
    public GiftCertificateDto findById(Long id) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate == null) {
            throw new GiftCertificateNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode());
        }
        return giftCertificateConverter.entityToDto(giftCertificate);
    }

    @Transactional
    @Override
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateConverter.dtoToEntity(giftCertificateDto);

        if (giftCertificateRepository.findByName(giftCertificate.getName()) != null) {
            throw new GiftCertificateAlreadyExistException(ServiceError.GIFT_CERTIFICATE_ALREADY_EXISTS.getCode());
        }

        Set<Tag> tags = new HashSet<>();
        if (giftCertificate.getTags() != null) {
            tags = mapNewTagsWithExisted(giftCertificate.getTags());
        }
        giftCertificate.setTags(tags);

        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);

        giftCertificate = giftCertificateRepository.save(giftCertificate);

        return giftCertificateConverter.entityToDto(giftCertificate);
    }

    @Transactional
    @Override
    public GiftCertificateDto update(Long id, GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate == null) {
            throw new GiftCertificateNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode());
        }
        Set<Tag> existedTags = giftCertificate.getTags();

        giftCertificate = giftCertificateConverter.dtoToEntity(giftCertificateDto);

        if (giftCertificate.getTags() != null) {
            existedTags.addAll(mapNewTagsWithExisted(giftCertificate.getTags()));
        }

        giftCertificate.setId(id);
        giftCertificate.setLastUpdateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));
        giftCertificate.setTags(existedTags);

        giftCertificate = giftCertificateRepository.update(giftCertificate);

        return giftCertificateConverter.entityToDto(giftCertificate);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate == null) {
            throw new GiftCertificateNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode());
        }
        giftCertificateRepository.delete(giftCertificate);
    }

    private Set<Tag> mapNewTagsWithExisted(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> {
                    Tag foundTag = tagRepository.findByName(tag.getName());
                    return foundTag == null ? tag : foundTag;
                }).collect(Collectors.toSet());
    }
}
