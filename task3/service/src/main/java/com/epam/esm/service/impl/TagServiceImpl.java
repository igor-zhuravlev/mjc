package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.tag.TagAlreadyExistException;
import com.epam.esm.service.exception.tag.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private Converter<Tag, TagDto> tagConverter;

    @Transactional(readOnly = true)
    @Override
    public List<TagDto> findAll(PageDto pageDto) {
        List<Tag> tags = tagRepository.findAll(pageDto.getOffset(), pageDto.getLimit());
        return tagConverter.entityToDtoList(tags);
    }

    @Transactional(readOnly = true)
    @Override
    public TagDto findById(Long id) {
        Tag tag = tagRepository.findById(id);
        if (tag == null) {
            throw new TagNotFoundException(ServiceError.TAG_NOT_FOUND.getCode());
        }
        return tagConverter.entityToDto(tag);
    }

    @Transactional
    @Override
    public TagDto create(TagDto tagDto) {
        Tag tag = tagConverter.dtoToEntity(tagDto);
        if (tagRepository.findByName(tag.getName()) != null) {
            throw new TagAlreadyExistException(ServiceError.TAG_ALREADY_EXISTS.getCode());
        }
        return tagConverter.entityToDto(tagRepository.save(tag));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Tag tag = tagRepository.findById(id);
        if (tag == null) {
            throw new TagNotFoundException(ServiceError.TAG_NOT_FOUND.getCode());
        }
        tagRepository.delete(tag);
    }
}
