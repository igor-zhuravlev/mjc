package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;

import java.util.List;

public interface TagService {
    List<TagDto> findAll(Integer offset, Integer limit);
    TagDto findById(Long id);
    TagDto save(TagDto tagDto);
    void deleteById(Long id);
}
