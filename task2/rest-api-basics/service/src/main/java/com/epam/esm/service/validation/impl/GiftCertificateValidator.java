package com.epam.esm.service.validation.impl;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class GiftCertificateValidator extends Validator<GiftCertificateDto> {

    @Override
    public boolean isValidToSave(GiftCertificateDto object) {
        return Stream.of(object.getName(), object.getDescription(),
                object.getPrice(), object.getDuration())
                .allMatch(Objects::nonNull);
    }

    @Override
    public boolean isValidToUpdate(GiftCertificateDto object) {
        return Stream.of(object.getName(), object.getDescription(),
                object.getPrice(), object.getDuration())
                .anyMatch(Objects::nonNull);
    }
}
