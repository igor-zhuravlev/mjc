package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ResourceAlreadyExistException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.exception.UnableDeleteResourceException;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<TagDto> findAll(Pageable page) {
        Page<Tag> tags = tagRepository.findAll(page);
        return tags.map(tag -> modelMapper.map(tag, TagDto.class));
    }

    @Transactional(readOnly = true)
    @Override
    public TagDto findById(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        Tag tag = optionalTag.orElseThrow(() ->
                new ResourceNotFoundException(ServiceError.TAG_NOT_FOUND.getCode()));
        return modelMapper.map(tag, TagDto.class);
    }

    @Transactional
    @Override
    public TagDto create(TagDto tagDto) {
        Tag tag = modelMapper.map(tagDto, Tag.class);
        if (tagRepository.findByName(tag.getName()) != null) {
            throw new ResourceAlreadyExistException(ServiceError.TAG_ALREADY_EXISTS.getCode());
        }
        Tag createdTag = tagRepository.save(tag);
        return modelMapper.map(createdTag, TagDto.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        Tag tag = optionalTag.orElseThrow(() ->
                new ResourceNotFoundException(ServiceError.TAG_NOT_FOUND.getCode()));
        if (!tag.getGiftCertificates().isEmpty()) {
            throw new UnableDeleteResourceException(ServiceError.TAG_UNABLE_DELETE.getCode());
        }
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true)
    @Override
    public TagDto findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() ->
                new ResourceNotFoundException(ServiceError.USER_NOT_FOUND.getCode()));
        Tag tag = tagRepository.findMostWidelyUsedTagWithHighestCostOfOrdersByUser(user);
        return modelMapper.map(tag, TagDto.class);
    }
}
