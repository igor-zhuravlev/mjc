package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import com.epam.esm.service.TagService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.tag.TagAlreadyExistException;
import com.epam.esm.service.exception.tag.TagNotFoundException;
import com.epam.esm.service.exception.tag.UnableDeleteTagException;
import com.epam.esm.service.exception.validation.TagNotValidException;
import com.epam.esm.service.util.ParamsUtil;
import com.epam.esm.service.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.anyLong;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private Converter<Tag, TagDto> tagConverter;
    @Mock
    private Validator<TagDto> tagDtoValidator;

    @InjectMocks
    private final TagService tagService = new TagServiceImpl();

    private static List<Tag> tagList;
    private static List<TagDto> tagDtoList;

    @BeforeAll
    static void beforeAll() {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("tag1");

        Tag tag2 = new Tag();
        tag1.setId(2L);
        tag1.setName("tag2");

        tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);

        TagDto tagDto1 = new TagDto();
        tagDto1.setName("tag1");

        TagDto tagDto2 = new TagDto();
        tagDto2.setName("tag2");

        tagDtoList = new ArrayList<>();
        tagDtoList.add(tagDto1);
        tagDtoList.add(tagDto2);
    }

    @Test
    void findAll_FoundAllTag_ReturnListOfTags() {
        given(tagRepository.findAll()).willReturn(tagList);
        given(tagConverter.entityToDtoList(tagList)).willReturn(tagDtoList);

        List<TagDto> actual = tagService.findAll();

        assertEquals(tagDtoList, actual);

        then(tagRepository)
                .should(only())
                .findAll();

        then(tagConverter)
                .should(only())
                .entityToDtoList(anyList());
    }

    @Test
    void findById_TagExist_ReturnFoundTag() {
        final Long id = 1L;

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");

        TagDto tagDto = new TagDto("tag1");

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(tagRepository.find(criteria)).willReturn(tag);
        given(tagConverter.entityToDto(tag)).willReturn(tagDto);

        TagDto actual = tagService.findById(id);

        assertEquals(tagDto, actual);

        then(tagRepository)
                .should(only())
                .find(any(Criteria.class));

        then(tagConverter)
                .should(only())
                .entityToDto(any(Tag.class));
    }

    @Test
    void findById_TagNotFound_TagNotFoundExceptionThrown() {
        final Long id = 1L;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(tagRepository.find(criteria)).willReturn(null);

        assertThrows(TagNotFoundException.class, () -> {
            tagService.findById(id);
        });

        then(tagRepository)
                .should(only())
                .find(any(Criteria.class));

        then(tagConverter)
                .should(never())
                .entityToDto(any(Tag.class));
    }

    @Test
    void save_SavingTag_ReturnSavedTag() {
        TagDto tagDto = new TagDto("tag1");

        Tag tag = new Tag();
        tag.setName("tag1");

        Tag savedTag = new Tag();
        savedTag.setId(1L);
        savedTag.setName("tag1");

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.NAME, tag.getName());

        given(tagDtoValidator.isValidToSave(tagDto)).willReturn(true);
        given(tagConverter.dtoToEntity(tagDto)).willReturn(tag);
        given(tagRepository.find(criteria)).willReturn(null);
        given(tagRepository.save(tag)).willReturn(savedTag);
        given(tagConverter.entityToDto(savedTag)).willReturn(tagDto);

        TagDto actual = tagService.save(tagDto);

        assertNotNull(actual);
        assertEquals(tagDto, actual);

        then(tagDtoValidator)
                .should(only())
                .isValidToSave(any(TagDto.class));

        then(tagConverter)
                .should(times(1))
                .dtoToEntity(any(TagDto.class));

        then(tagRepository)
                .should(times(1))
                .find(any(Criteria.class));

        then(tagRepository)
                .should(times(1))
                .save(any(Tag.class));

        then(tagConverter)
                .should(times(1))
                .entityToDto(any(Tag.class));
    }

    @Test
    void save_TagAlreadyExist_TagAlreadyExistExceptionThrown() {
        TagDto tagDto = new TagDto("tag1");

        Tag tag = new Tag();
        tag.setName("tag1");

        Tag existedTag = new Tag();
        existedTag.setId(1L);
        existedTag.setName("tag1");

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.NAME, tag.getName());

        given(tagDtoValidator.isValidToSave(tagDto)).willReturn(true);
        given(tagConverter.dtoToEntity(tagDto)).willReturn(tag);
        given(tagRepository.find(criteria)).willReturn(existedTag);

        assertThrows(TagAlreadyExistException.class, () -> {
            tagService.save(tagDto);
        });

        then(tagDtoValidator)
                .should(only())
                .isValidToSave(any(TagDto.class));

        then(tagConverter)
                .should(only())
                .dtoToEntity(any(TagDto.class));

        then(tagRepository)
                .should(only())
                .find(any(Criteria.class));

        then(tagRepository)
                .should(never())
                .save(any(Tag.class));

        then(tagConverter)
                .should(never())
                .entityToDto(any(Tag.class));
    }

    @Test
    void save_InvalidTag_TagNotValidExceptionThrown() {
        TagDto tagDto = new TagDto();

        given(tagDtoValidator.isValidToSave(tagDto)).willReturn(false);

        assertThrows(TagNotValidException.class, () -> {
            tagService.save(tagDto);
        });

        then(tagDtoValidator)
                .should(only())
                .isValidToSave(any(TagDto.class));

        then(tagConverter)
                .should(never())
                .dtoToEntity(any(TagDto.class));

        then(tagRepository)
                .should(never())
                .find(any());

        then(tagRepository)
                .should(never())
                .save(any(Tag.class));

        then(tagConverter)
                .should(never())
                .entityToDto(any(Tag.class));
    }

    @Test
    void deleteById_FoundExistedTag_ReturnNothing() {
        final Long id = 1L;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");

        given(tagRepository.find(criteria)).willReturn(tag);
        given(tagRepository.deleteById(id)).willReturn(1L);

        tagService.deleteById(id);

        then(tagRepository)
                .should(times(1))
                .find(any(Criteria.class));

        then(tagRepository)
                .should(times(1))
                .deleteById(anyLong());
    }

    @Test
    void deleteById_TagNotExist_TagNotFoundExceptionThrown() {
        final Long id = 1L;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        given(tagRepository.find(criteria)).willReturn(null);

        assertThrows(TagNotFoundException.class, () -> {
            tagService.deleteById(id);
        });

        then(tagRepository)
                .should(only())
                .find(any(Criteria.class));

        then(tagRepository)
                .should(never())
                .deleteById(anyLong());
    }

    @Test
    void deleteById_TagNoDeleted_UnableDeleteTagExceptionThrown() {
        final Long id = 1L;

        Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(id));

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");

        given(tagRepository.find(criteria)).willReturn(tag);
        given(tagRepository.deleteById(id)).willReturn(0L);

        assertThrows(UnableDeleteTagException.class, () -> {
            tagService.deleteById(id);
        });

        then(tagRepository)
                .should(times(1))
                .find(any(Criteria.class));

        then(tagRepository)
                .should(times(1))
                .deleteById(anyLong());
    }
}