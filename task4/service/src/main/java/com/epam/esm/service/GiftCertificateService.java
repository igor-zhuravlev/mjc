package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import com.epam.esm.service.dto.GiftCertificateUpdateDto;
import com.epam.esm.service.dto.PageDto;
import org.springframework.data.domain.Page;

public interface GiftCertificateService extends Service<GiftCertificateDto, Long> {
    Page<GiftCertificateDto> findAll(GiftCertificateParamDto giftCertificateParam, PageDto pageDto);
    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);
    GiftCertificateDto update(Long id, GiftCertificateUpdateDto giftCertificateDto);
    void delete(Long id);
}
