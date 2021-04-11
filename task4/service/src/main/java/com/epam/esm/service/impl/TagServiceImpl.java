package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.tag.TagAlreadyExistException;
import com.epam.esm.service.exception.tag.TagNotFoundException;
import com.epam.esm.service.exception.tag.UnableDeleteTagException;
import com.epam.esm.service.exception.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Converter<Tag, TagDto> tagConverter;

    @Transactional(readOnly = true)
    @Override
    public Page<TagDto> findAll(Pageable page) {
        Page<Tag> tags = tagRepository.findAll(page);
        return tags.map(tagConverter::entityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public TagDto findById(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        Tag tag = optionalTag.orElseThrow(() ->
                new TagNotFoundException(ServiceError.TAG_NOT_FOUND.getCode()));
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
        Optional<Tag> optionalTag = tagRepository.findById(id);
        Tag tag = optionalTag.orElseThrow(() ->
                new TagNotFoundException(ServiceError.TAG_NOT_FOUND.getCode()));
        if (!tag.getGiftCertificates().isEmpty()) {
            throw new UnableDeleteTagException(ServiceError.TAG_UNABLE_DELETE.getCode());
        }
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true)
    @Override
    public TagDto findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() ->
                new UserNotFoundException(ServiceError.USER_NOT_FOUND.getCode()));
        Tag tag = tagRepository.findMostWidelyUsedTagWithHighestCostOfOrdersByUser(user);
        return tagConverter.entityToDto(tag);
    }
}
