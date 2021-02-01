package com.epam.esm.service.validation.impl;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class TagValidator extends Validator<TagDto> {

    @Override
    public boolean isValidToSave(TagDto object) {
        return object.getName() != null;
    }
}
