package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import com.epam.esm.service.dto.PageDto;

import java.util.List;

public interface GiftCertificateService extends Service<GiftCertificateDto, Long> {
    List<GiftCertificateDto> findAll(GiftCertificateParamDto giftCertificateParam, PageDto pageDto);
    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);
    GiftCertificateDto update(Long id, GiftCertificateDto giftCertificateDto);
    void delete(Long id);
}
