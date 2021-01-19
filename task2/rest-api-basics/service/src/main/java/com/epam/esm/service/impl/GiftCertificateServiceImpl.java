package com.epam.esm.service.impl;

import com.epam.esm.constant.ServiceError;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.converter.Converter;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.util.Criteria;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.certificate.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.certificate.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.certificate.UnableDeleteGiftCertificateException;
import com.epam.esm.service.exception.certificate.UnableUpdateGiftCertificate;
import com.epam.esm.util.ParamsUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final Logger logger = LogManager.getLogger(GiftCertificateServiceImpl.class);

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private Converter<GiftCertificate, GiftCertificateDto> giftCertificateConverter;

    @Transactional(readOnly = true)
    @Override
    public List<GiftCertificateDto> findAll(Map<String, String[]> params) throws ServiceException {
        try {
            // TODO: 13-Jan-21 validate params
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
            // TODO: 16-Jan-21 validate name
            GiftCertificate giftCertificate = giftCertificateRepository.findById(id);
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
            // TODO: 16-Jan-21 validate giftCertificateDto
            GiftCertificate giftCertificate = giftCertificateConverter.dtoToEntity(giftCertificateDto);

            if (giftCertificateRepository.findByName(giftCertificate.getName()) != null) {
                throw new GiftCertificateAlreadyExistException(ServiceError.GIFT_CERTIFICATE_ALREADY_EXISTS.getCode());
            }

            if (giftCertificate.getTags() != null) {
                Set<Tag> tags = giftCertificate.getTags().stream()
                        .map(tag -> tagRepository.save(tag))
                        .collect(Collectors.toSet());
                giftCertificate.setTags(tags);
            }

            giftCertificate.setCreateDate(LocalDateTime.now());
            giftCertificate = giftCertificateRepository.save(giftCertificate);

            return giftCertificateConverter.entityToDto(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public GiftCertificateDto updateById(Long id, GiftCertificateDto giftCertificateDto) throws ServiceException {
        try {
            // TODO: 16-Jan-21 validate name, giftCertificateDto
            GiftCertificate giftCertificate = giftCertificateRepository.findById(id);

            if (giftCertificate == null) {
                throw new GiftCertificateNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode());
            }

            giftCertificate = giftCertificateConverter.dtoToEntity(giftCertificateDto);

            Set<Tag> newTags = new HashSet<>();
            if (giftCertificate.getTags() != null) {
                newTags = giftCertificate.getTags().stream()
                        .map(tag -> tagRepository.save(tag))
                        .collect(Collectors.toSet());
                giftCertificate.setTags(newTags);
            }

            giftCertificate.setId(id);
            giftCertificate.setLastUpdateDate(LocalDateTime.now());
            final long count = giftCertificateRepository.update(giftCertificate);

            if (count == 0) {
                throw new UnableUpdateGiftCertificate(ServiceError.UNABLE_UPDATE_GIFT_CERTIFICATE.getCode());
            }

            giftCertificate = giftCertificateRepository.findById(id);
            giftCertificate.setTags(newTags);

            return giftCertificateConverter.entityToDto(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            // TODO: 16-Jan-21 validate name
            GiftCertificate giftCertificate = giftCertificateRepository.findById(id);
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
