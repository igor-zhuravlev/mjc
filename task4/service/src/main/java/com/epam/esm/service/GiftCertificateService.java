package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import com.epam.esm.service.dto.GiftCertificateUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftCertificateService extends Service<GiftCertificateDto, Long> {
    Page<GiftCertificateDto> findAll(GiftCertificateParamDto giftCertificateParam, Pageable page);
    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);
    GiftCertificateDto update(Long id, GiftCertificateUpdateDto giftCertificateDto);
    void delete(Long id);
}
