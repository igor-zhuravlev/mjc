package com.epam.esm.service.impl;

import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.certificate.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.certificate.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.certificate.UnableDeleteGiftCertificateException;
import com.epam.esm.service.exception.certificate.UnableUpdateGiftCertificate;
import com.epam.esm.service.exception.validation.GiftCertificateNotValidException;
import com.epam.esm.service.exception.validation.GiftCertificateParamsNotValidException;
import com.epam.esm.service.util.ParamsUtil;
import com.epam.esm.service.validation.ParamsValidator;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
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
    private Validator<GiftCertificateDto> giftCertificateDtoValidator;
    @Autowired
    private ParamsValidator paramsValidator;

    @Transactional(readOnly = true)
    @Override
    public List<GiftCertificateDto> findAll(Map<String, String[]> params) throws ServiceException {
        try {
            if (!paramsValidator.isValid(params)) {
                throw new GiftCertificateParamsNotValidException(ServiceError.GIFT_CERTIFICATE_PARAMS_NOT_VALID.getCode());
            }
            Criteria criteria = ParamsUtil.buildCriteria(params);
            List<GiftCertificate> giftCertificateList = giftCertificateRepository.findAll(criteria);
            return giftCertificateConverter.entityToDtoList(giftCertificateList);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public GiftCertificateDto findById(Long id) throws ServiceException {
        try {
            Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));
            GiftCertificate giftCertificate = giftCertificateRepository.find(criteria);
            if (giftCertificate == null) {
                throw new GiftCertificateNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode());
            }
            return giftCertificateConverter.entityToDto(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public GiftCertificateDto save(GiftCertificateDto giftCertificateDto) throws ServiceException {
        try {
            if (!giftCertificateDtoValidator.isValidToSave(giftCertificateDto)) {
                throw new GiftCertificateNotValidException(ServiceError.GIFT_CERTIFICATE_NOT_VALID.getCode());
            }

            GiftCertificate giftCertificate = giftCertificateConverter.dtoToEntity(giftCertificateDto);

            Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.NAME, giftCertificate.getName());
            if (giftCertificateRepository.find(criteria) != null) {
                throw new GiftCertificateAlreadyExistException(ServiceError.GIFT_CERTIFICATE_ALREADY_EXISTS.getCode());
            }

            giftCertificate.setCreateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));
            giftCertificate = giftCertificateRepository.save(giftCertificate);

            if (giftCertificate.getTags() != null) {
                Set<Tag> tags = giftCertificate.getTags().stream()
                        .map(tag -> {
                            Criteria tagCriteria = ParamsUtil.buildCriteria(CriteriaSearch.NAME, tag.getName());
                            Tag foundTag = tagRepository.find(tagCriteria);
                            return foundTag == null ? tagRepository.save(tag) : foundTag;
                        })
                        .collect(Collectors.toSet());
                giftCertificate.setTags(tags);
                giftCertificateRepository.bindGiftCertificateWithTags(giftCertificate.getId(), tags);
            }

            return giftCertificateConverter.entityToDto(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public GiftCertificateDto updateById(Long id, GiftCertificateDto giftCertificateDto) throws ServiceException {
        try {
            if (!giftCertificateDtoValidator.isValidToUpdate(giftCertificateDto)) {
                throw new GiftCertificateNotValidException(ServiceError.GIFT_CERTIFICATE_NOT_VALID.getCode());
            }

            Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));
            GiftCertificate giftCertificate = giftCertificateRepository.find(criteria);

            if (giftCertificate == null) {
                throw new GiftCertificateNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode());
            }

            Set<Tag> existedTags = new HashSet<>();
            if (giftCertificate.getTags() != null) {
                existedTags.addAll(giftCertificate.getTags());
            }

            giftCertificate = giftCertificateConverter.dtoToEntity(giftCertificateDto);

            giftCertificate.setId(id);
            giftCertificate.setLastUpdateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));
            final long count = giftCertificateRepository.update(giftCertificate);

            if (count == 0) {
                throw new UnableUpdateGiftCertificate(ServiceError.GIFT_CERTIFICATE_UNABLE_UPDATE.getCode());
            }

            if (giftCertificate.getTags() != null) {
                Set<Tag> tags = giftCertificate.getTags().stream()
                        .map(tag -> {
                            Criteria tagCriteria = ParamsUtil.buildCriteria(CriteriaSearch.NAME, tag.getName());
                            Tag foundTag = tagRepository.find(tagCriteria);
                            return foundTag == null ? tagRepository.save(tag) : foundTag;
                        })
                        .filter(tag -> !existedTags.contains(tag))
                        .collect(Collectors.toSet());
                giftCertificateRepository.bindGiftCertificateWithTags(giftCertificate.getId(), tags);
            }

            criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));
            giftCertificate = giftCertificateRepository.find(criteria);

            return giftCertificateConverter.entityToDto(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));
            GiftCertificate giftCertificate = giftCertificateRepository.find(criteria);
            if (giftCertificate == null) {
                throw new GiftCertificateNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode());
            }
            final long count = giftCertificateRepository.deleteById(id);
            if (count == 0) {
                throw new UnableDeleteGiftCertificateException(ServiceError.GIFT_CERTIFICATE_UNABLE_DELETE.getCode());
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
