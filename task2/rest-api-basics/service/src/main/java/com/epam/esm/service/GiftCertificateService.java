package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService extends Service<GiftCertificateDto, Long> {
    List<GiftCertificateDto> findAll(Map<String, String[]> params) throws ServiceException;
    GiftCertificateDto updateById(Long id, GiftCertificateDto giftCertificateDto) throws ServiceException;
}
