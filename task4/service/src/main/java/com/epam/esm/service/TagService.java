package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService extends Service<TagDto, Long> {
    Page<TagDto> findAll(Pageable page);
    TagDto create(TagDto tagDto);
    void delete(Long id);
    TagDto findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(Long userId);
}
