package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.PageDto;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService extends Service<GiftCertificateDto, Long> {
    List<GiftCertificateDto> findAll(Map<String, String[]> params, PageDto pageDto);
    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);
    GiftCertificateDto update(Long id, GiftCertificateDto giftCertificateDto);
    void delete(Long id);
}
