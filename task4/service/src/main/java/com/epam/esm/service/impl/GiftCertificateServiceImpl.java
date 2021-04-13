package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.specification.impl.GiftCertificateSpecificationImpl;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import com.epam.esm.service.dto.GiftCertificateUpdateDto;
import com.epam.esm.service.exception.ResourceAlreadyExistException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.exception.UnableDeleteResourceException;
import com.epam.esm.service.util.GiftCertificateCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private Converter<GiftCertificate, GiftCertificateDto> giftCertificateConverter;
    @Autowired
    private Converter<GiftCertificate, GiftCertificateUpdateDto> giftCertificateUpdateConverter;

    @Transactional(readOnly = true)
    @Override
    public Page<GiftCertificateDto> findAll(GiftCertificateParamDto giftCertificateParam, Pageable page) {
        GiftCertificateCriteria criteria = GiftCertificateCriteriaBuilder.build(giftCertificateParam);
        Specification<GiftCertificate> specification = new GiftCertificateSpecificationImpl(criteria);
        Page<GiftCertificate> giftCertificatePage = giftCertificateRepository.findAll(specification,
                PageRequest.of(page.getPageNumber(), page.getPageSize(), criteria.getSort()));
        return giftCertificatePage.map(giftCertificateConverter::entityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public GiftCertificateDto findById(Long id) {
        Optional<GiftCertificate> giftCertificateOptional =
                giftCertificateRepository.findById(id);
        GiftCertificate giftCertificate = giftCertificateOptional.orElseThrow(() ->
                new ResourceNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode()));
        return giftCertificateConverter.entityToDto(giftCertificate);
    }

    @Transactional
    @Override
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateConverter.dtoToEntity(giftCertificateDto);

        if (giftCertificateRepository.findByName(giftCertificate.getName()) != null) {
            throw new ResourceAlreadyExistException(ServiceError.GIFT_CERTIFICATE_ALREADY_EXISTS.getCode());
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
    public GiftCertificateDto update(Long id, GiftCertificateUpdateDto giftCertificateDto) {
        Optional<GiftCertificate> existedGiftCertificateOptional = giftCertificateRepository.findById(id);

        GiftCertificate existedGiftCertificate = existedGiftCertificateOptional.orElseThrow(() ->
                        new ResourceNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode()));

        GiftCertificate giftCertificate = giftCertificateUpdateConverter.dtoToEntity(giftCertificateDto);

        GiftCertificate updatedGiftCertificate = mapUpdatedFields(existedGiftCertificate, giftCertificate);
        updatedGiftCertificate.setLastUpdateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));

        updatedGiftCertificate = giftCertificateRepository.save(updatedGiftCertificate);
        giftCertificateRepository.findById(1L);
        giftCertificateRepository.getOne(1L);

        return giftCertificateConverter.entityToDto(updatedGiftCertificate);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(id);
        GiftCertificate giftCertificate = giftCertificateOptional.orElseThrow(() ->
                new ResourceNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode()));
        if (!giftCertificate.getOrderGiftCertificates().isEmpty()) {
            throw new UnableDeleteResourceException(ServiceError.GIFT_CERTIFICATE_UNABLE_DELETE.getCode());
        }
        giftCertificateRepository.delete(giftCertificate);
    }

    private GiftCertificate mapUpdatedFields(GiftCertificate existedGiftCertificate, GiftCertificate giftCertificate) {
        if (giftCertificate.getName() != null) {
            existedGiftCertificate.setName(giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            existedGiftCertificate.setDescription(giftCertificate.getDescription());
        }
        if (giftCertificate.getPrice() != null) {
            existedGiftCertificate.setPrice(giftCertificate.getPrice());
        }
        if (giftCertificate.getDuration() != null) {
            existedGiftCertificate.setDuration(giftCertificate.getDuration());
        }
        if (giftCertificate.getTags() != null) {
            existedGiftCertificate.setTags(mapNewTagsWithExisted(giftCertificate.getTags()));
        }
        return existedGiftCertificate;
    }

    private Set<Tag> mapNewTagsWithExisted(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> {
                    Tag foundTag = tagRepository.findByName(tag.getName());
                    return foundTag == null ? tag : foundTag;
                }).collect(Collectors.toSet());
    }
}
