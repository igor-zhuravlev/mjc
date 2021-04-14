package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ResourceAlreadyExistException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.exception.UnableDeleteResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private final TagService tagService = new TagServiceImpl();

    @Test
    void findById_TagExist_ReturnFoundTag() {
        final Long id = 1L;

        Optional<Tag> tagOptional = Optional.of(new Tag());
        TagDto tagDto = new TagDto();

        given(tagRepository.findById(id))
                .willReturn(tagOptional);
        given(modelMapper.map(tagOptional.get(), TagDto.class))
                .willReturn(tagDto);

        tagService.findById(id);

        then(tagRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(only())
                .map(any(Tag.class), eq(TagDto.class));
    }

    @Test
    void findById_TagNotFound_ResourceNotFoundExceptionThrown() {
        final Long id = 1L;

        given(tagRepository.findById(id))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tagService.findById(id);
        });

        then(tagRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(never())
                .map(any(Tag.class), eq(TagDto.class));
    }

    @Test
    void create_CreatingTag_ReturnCreatedTag() {
        final String tagName = "tag1";

        TagDto tagDto = new TagDto();
        tagDto.setName(tagName);

        Tag tag = new Tag();
        tag.setName(tagName);

        Tag createdTag = new Tag();
        createdTag.setId(1L);
        createdTag.setName(tagName);

        TagDto createdTagDto = new TagDto();
        createdTagDto.setId(1L);
        createdTagDto.setName(tagName);

        given(modelMapper.map(tagDto, Tag.class))
                .willReturn(tag);
        given(tagRepository.findByName(tag.getName()))
                .willReturn(null);
        given(tagRepository.save(tag))
                .willReturn(createdTag);
        given(modelMapper.map(createdTag, TagDto.class))
                .willReturn(createdTagDto);

        tagService.create(tagDto);

        then(modelMapper)
                .should(times(1))
                .map(any(TagDto.class), eq(Tag.class));
        then(tagRepository)
                .should(times(1))
                .findByName(anyString());
        then(tagRepository)
                .should(times(1))
                .save(any(Tag.class));
        then(modelMapper)
                .should(times(1))
                .map(any(Tag.class), eq(TagDto.class));
    }

    @Test
    void create_TagAlreadyExist_ResourceAlreadyExistExceptionThrown() {
        final String name = "tag1";

        TagDto tagDto = new TagDto();
        tagDto.setName(name);

        Tag tag = new Tag();
        tag.setName(name);

        Tag existedTag = new Tag();
        existedTag.setId(1L);
        existedTag.setName(name);

        given(modelMapper.map(tagDto, Tag.class))
                .willReturn(tag);
        given(tagRepository.findByName(tag.getName()))
                .willReturn(existedTag);

        assertThrows(ResourceAlreadyExistException.class, () -> {
            tagService.create(tagDto);
        });

        then(modelMapper)
                .should(only())
                .map(any(TagDto.class), eq(Tag.class));
        then(tagRepository)
                .should(only())
                .findByName(anyString());
        then(tagRepository)
                .should(never())
                .save(any(Tag.class));
        then(modelMapper)
                .should(never())
                .map(any(Tag.class), eq(TagDto.class));
    }

    @Test
    void delete_FoundExistedTag_ReturnNothing() {
        final Long id = 1L;
        final String name = "tag1";

        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        tag.setGiftCertificates(new HashSet<>());

        given(tagRepository.findById(id))
                .willReturn(Optional.of(tag));
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
    void delete_TagNotExist_ResourceNotFoundExceptionThrown() {
        final Long id = 1L;

        given(tagRepository.findById(id))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
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
    void delete_TagUsedByCertificate_UnableDeleteResourceExceptionThrown() {
        final Long id = 1L;
        final String name = "tag1";

        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        tag.setGiftCertificates(Set.of(new GiftCertificate()));

        given(tagRepository.findById(id))
                .willReturn(Optional.of(tag));

        assertThrows(UnableDeleteResourceException.class, () -> {
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
