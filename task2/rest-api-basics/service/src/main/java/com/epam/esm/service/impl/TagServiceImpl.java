package com.epam.esm.service.impl;

import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.tag.TagAlreadyExistException;
import com.epam.esm.service.exception.tag.TagNotFoundException;
import com.epam.esm.service.exception.validation.TagNotValidException;
import com.epam.esm.service.exception.tag.UnableDeleteTagException;
import com.epam.esm.service.util.ParamsUtil;
import com.epam.esm.service.validation.Validator;
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
    @Autowired
    private Validator<TagDto> tagDtoValidator;

    @Transactional(readOnly = true)
    @Override
    public List<TagDto> findAll() throws ServiceException {
        try {
            List<Tag> tags = tagRepository.findAll();
            return tagConverter.entityToDtoList(tags);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public TagDto findById(Long id) throws ServiceException {
        try {
            Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));
            Tag tag = tagRepository.find(criteria);
            if (tag == null) {
                throw new TagNotFoundException(ServiceError.TAG_NOT_FOUND.getCode());
            }
            return tagConverter.entityToDto(tag);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public TagDto save(TagDto tagDto) throws ServiceException {
        try {
            if (!tagDtoValidator.isValidToSave(tagDto)) {
                throw new TagNotValidException(ServiceError.TAG_NOT_VALID.getCode());
            }
            Tag tag = tagConverter.dtoToEntity(tagDto);
            Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.NAME, tag.getName());
            if (tagRepository.find(criteria) != null) {
                throw new TagAlreadyExistException(ServiceError.TAG_ALREADY_EXISTS.getCode());
            }
            return tagConverter.entityToDto(tagRepository.save(tag));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));
            Tag tag = tagRepository.find(criteria);
            if (tag == null) {
                throw new TagNotFoundException(ServiceError.TAG_NOT_FOUND.getCode());
            }
            long count = tagRepository.deleteById(id);
            if (count == 0) {
                throw new UnableDeleteTagException(ServiceError.TAG_UNABLE_DELETE.getCode());
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
