package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {
    List<GiftCertificateDto> findAll(Map<String, String[]> params);
    GiftCertificateDto findById(Long id);
    GiftCertificateDto save(GiftCertificateDto giftCertificateDto);
    GiftCertificateDto updateById(Long id, GiftCertificateDto giftCertificateDto);
    void deleteById(Long id);
}
