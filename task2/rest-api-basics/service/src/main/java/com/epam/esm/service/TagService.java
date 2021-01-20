package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface TagService extends Service<TagDto, Long> {
    List<TagDto> findAll() throws ServiceException;
}
