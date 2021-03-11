package com.epam.esm.service.converter.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.GiftCertificateUpdateDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiftCertificateUpdateConverter implements Converter<GiftCertificate, GiftCertificateUpdateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GiftCertificate dtoToEntity(GiftCertificateUpdateDto dto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        modelMapper.map(dto, giftCertificate);
        return giftCertificate;
    }

    @Override
    public GiftCertificateUpdateDto entityToDto(GiftCertificate entity) {
        GiftCertificateUpdateDto giftCertificateUpdateDto = new GiftCertificateUpdateDto();
        modelMapper.map(entity, giftCertificateUpdateDto);
        return giftCertificateUpdateDto;
    }

    @Override
    public List<GiftCertificate> dtoToEntityList(List<GiftCertificateUpdateDto> dtoList) {
        return dtoList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateUpdateDto> entityToDtoList(List<GiftCertificate> entityList) {
        return entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
