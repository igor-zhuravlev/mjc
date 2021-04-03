package com.epam.esm.service;

import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.TagDto;
import org.springframework.data.domain.Page;

public interface TagService extends Service<TagDto, Long> {
    Page<TagDto> findAll(PageDto pageDto);
    TagDto create(TagDto tagDto);
    void delete(Long id);
    TagDto findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(Long userId);
}
