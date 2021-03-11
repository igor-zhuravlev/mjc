package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.tag.TagAlreadyExistException;
import com.epam.esm.service.exception.tag.TagNotFoundException;
import com.epam.esm.service.exception.tag.UnableDeleteTagException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private Converter<Tag, TagDto> tagConverter;

    @InjectMocks
    private final TagService tagService = new TagServiceImpl();

    @Test
    void findAll_FoundAllTag_ReturnListOfTags() {
        PageDto pageDto = new PageDto(5, 1);

        List<Tag> tagList = new ArrayList<>();
        List<TagDto> tagDtoList = new ArrayList<>();
        for (long i = 1; i <= 2; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setName("tag" + i);

            TagDto tagDto = new TagDto();
            tagDto.setId(i);
            tagDto.setName("tag" + i);

            tagList.add(tag);
            tagDtoList.add(tagDto);
        }

        given(tagRepository.findAll(anyInt(), anyInt()))
                .willReturn(tagList);
        given(tagConverter.entityToDtoList(tagList))
                .willReturn(tagDtoList);

        List<TagDto> actual = tagService.findAll(pageDto);

        assertNotNull(actual);
        assertEquals(tagDtoList, actual);

        then(tagRepository)
                .should(only())
                .findAll(anyInt(), anyInt());

        then(tagConverter)
                .should(only())
                .entityToDtoList(anyList());
    }

    @Test
    void findById_TagExist_ReturnFoundTag() {
        final Long id = 1L;
        final String name = "tag1";

        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);

        TagDto tagDto = new TagDto();
        tagDto.setId(id);
        tagDto.setName(name);

        given(tagRepository.findById(id)).willReturn(tag);
        given(tagConverter.entityToDto(tag)).willReturn(tagDto);

        TagDto actual = tagService.findById(id);

        assertEquals(tagDto, actual);

        then(tagRepository)
                .should(only())
                .findById(anyLong());

        then(tagConverter)
                .should(only())
                .entityToDto(any(Tag.class));
    }

    @Test
    void findById_TagNotFound_TagNotFoundExceptionThrown() {
        final Long id = 1L;
        final String name = "tag1";

        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);

        given(tagRepository.findById(id)).willReturn(null);

        assertThrows(TagNotFoundException.class, () -> {
            tagService.findById(id);
        });

        then(tagRepository)
                .should(only())
                .findById(anyLong());

        then(tagConverter)
                .should(never())
                .entityToDto(any(Tag.class));
    }

    @Test
    void create_CreatingTag_ReturnCreatedTag() {
        final String name = "tag1";

        TagDto tagDto = new TagDto();
        tagDto.setName(name);

        Tag tag = new Tag();
        tag.setName(name);

        Tag createdTag = new Tag();
        createdTag.setId(1L);
        createdTag.setName(name);

        TagDto createdTagDto = new TagDto();
        createdTagDto.setId(1L);
        createdTagDto.setName(name);

        given(tagConverter.dtoToEntity(tagDto))
                .willReturn(tag);
        given(tagRepository.findByName(tag.getName()))
                .willReturn(null);
        given(tagRepository.save(tag))
                .willReturn(createdTag);
        given(tagConverter.entityToDto(createdTag))
                .willReturn(createdTagDto);

        TagDto actual = tagService.create(tagDto);

        assertNotNull(actual);
        assertEquals(createdTagDto, actual);

        then(tagConverter)
                .should(times(1))
                .dtoToEntity(any(TagDto.class));

        then(tagRepository)
                .should(times(1))
                .findByName(anyString());

        then(tagRepository)
                .should(times(1))
                .save(any(Tag.class));

        then(tagConverter)
                .should(times(1))
                .entityToDto(any(Tag.class));
    }

    @Test
    void create_TagAlreadyExist_TagAlreadyExistExceptionThrown() {
        final String name = "tag1";

        TagDto tagDto = new TagDto();
        tagDto.setName(name);

        Tag tag = new Tag();
        tag.setName(name);

        Tag existedTag = new Tag();
        existedTag.setId(1L);
        existedTag.setName(name);

        given(tagConverter.dtoToEntity(tagDto))
                .willReturn(tag);
        given(tagRepository.findByName(tag.getName()))
                .willReturn(existedTag);

        assertThrows(TagAlreadyExistException.class, () -> {
            tagService.create(tagDto);
        });

        then(tagConverter)
                .should(only())
                .dtoToEntity(any(TagDto.class));

        then(tagRepository)
                .should(only())
                .findByName(anyString());

        then(tagRepository)
                .should(never())
                .save(any(Tag.class));

        then(tagConverter)
                .should(never())
                .entityToDto(any(Tag.class));
    }

    @Test
    void delete_FoundExistedTag_ReturnNothing() {
        final Long id = 1L;
        final String name = "tag1";

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName(name);
        tag.setGiftCertificates(new HashSet<>());

        given(tagRepository.findById(id)).willReturn(tag);
        willDoNothing().given(tagRepository).delete(tag);

        tagService.delete(id);

        then(tagRepository)
                .should(times(1))
                .findById(anyLong());

        then(tagRepository)
                .should(times(1))
                .delete(any(Tag.class));
    }

    @Test
    void delete_TagNotExist_TagNotFoundExceptionThrown() {
        final Long id = 1L;

        given(tagRepository.findById(id)).willReturn(null);

        assertThrows(TagNotFoundException.class, () -> {
            tagService.delete(id);
        });

        then(tagRepository)
                .should(only())
                .findById(anyLong());

        then(tagRepository)
                .should(never())
                .delete(any(Tag.class));
    }

    @Test
    void delete_TagUsedByCertificate_UnableDeleteTagExceptionThrown() {
        final Long id = 1L;
        final String name = "tag1";

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName(name);
        tag.setGiftCertificates(Set.of(new GiftCertificate()));

        given(tagRepository.findById(id)).willReturn(tag);

        assertThrows(UnableDeleteTagException.class, () -> {
            tagService.delete(id);
        });

        then(tagRepository)
                .should(only())
                .findById(anyLong());

        then(tagRepository)
                .should(never())
                .delete(any(Tag.class));
    }
}