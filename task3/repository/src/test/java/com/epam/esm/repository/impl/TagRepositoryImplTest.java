package com.epam.esm.repository.impl;

import com.epam.esm.domain.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.config.RepositoryTestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
class TagRepositoryImplTest {

    @Autowired
    private TagRepository tagRepository;

    private List<Tag> tagList;

    private static int offset;
    private static int limit;

    @BeforeAll
    static void beforeAll() {
        offset = 0;
        limit = 5;
    }

    @BeforeEach
    void beforeEach() {
        tagList = tagRepository.findAll(offset, limit);
        assertNotEquals(0, tagList.size());
    }

    @Test
    void findAll_SearchForAllTagsWithOffsetAndLimit_ReturnFoundTagList() {
        List<Tag> actual = tagRepository.findAll(offset, limit);
        assertEquals(tagList, actual);
    }

    @Test
    void findById_SearchForTagById_ReturnFoundTag() {
        Tag tag = tagList.get(0);
        final Long id = tag.getId();

        Tag actual = tagRepository.findById(id);

        assertEquals(tag, actual);
    }

    @Test
    void findByName_SearchForTagByName_ReturnFoundTag() {
        Tag tag = tagList.get(0);
        final String name = tag.getName();

        Tag actual = tagRepository.findByName(name);

        assertEquals(tag, actual);
    }

    @Test
    void save_SavingTag_ReturnSavedTag() {
        final String tagName = "tag111";

        Tag tag = new Tag();
        tag.setName(tagName);

        Tag saved = tagRepository.save(tag);
        Tag actual = tagRepository.findByName(tagName);

        assertEquals(saved, actual);
    }

    @Test
    void delete_DeletingTag_ReturnNothing() {
        Tag tag = new Tag();
        tag.setName("tag222");

        Tag savedTag = tagRepository.save(tag);
        Long savedTagId = savedTag.getId();

        assertNotNull(savedTag);

        tagRepository.delete(savedTag);

        Tag actual = tagRepository.findById(savedTagId);

        assertNull(actual);
    }
}
