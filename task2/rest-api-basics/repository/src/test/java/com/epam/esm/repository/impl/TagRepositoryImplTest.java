package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.config.RepositoryTestConfig;
import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryTestConfig.class)
class TagRepositoryImplTest {

    @Autowired
    private TagRepository tagRepository;

    private List<Tag> tagList;

    @BeforeEach
    void beforeEach() {
        tagList = tagRepository.findAll();

        assertNotEquals(0, tagList.size());
    }

    @Test
    void findAll_SearchForAllTags_ReturnFoundTagList() {
        List<Tag> actual = tagRepository.findAll();

        assertEquals(tagList, actual);
    }

    @Test
    void find_SearchForTagById_ReturnFoundTag() {
        Tag tag = tagList.get(0);
        final Long id = tag.getId();

        Map<CriteriaSearch, String> params = new HashMap<>();
        params.put(CriteriaSearch.ID, String.valueOf(id));
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        Tag actual = tagRepository.find(criteria);

        assertEquals(tag, actual);
    }

    @Test
    void find_SearchForTagByName_ReturnFoundTag() {
        Tag tag = tagList.get(0);
        final String name = tag.getName();

        Map<CriteriaSearch, String> params = new HashMap<>();
        params.put(CriteriaSearch.NAME, name);
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        Tag actual = tagRepository.find(criteria);

        assertEquals(tag, actual);
    }

    @Test
    void save_SavingTag_ReturnSavedTag() {
        Tag tag = new Tag("tag123");

        Tag saved = tagRepository.save(tag);

        Map<CriteriaSearch, String> params = new HashMap<>();
        params.put(CriteriaSearch.ID, String.valueOf(saved.getId()));
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        Tag actual = tagRepository.find(criteria);

        assertEquals(saved, actual);
    }

    @Test
    void deleteById_DeletingTag_ReturnDeletedRowCount() {
        final Long id = tagList.get(0).getId();

        Long count = tagRepository.deleteById(id);

        assertEquals(1L, count);

        Map<CriteriaSearch, String> params = new HashMap<>();
        params.put(CriteriaSearch.ID, String.valueOf(id));
        Criteria criteria = new Criteria();
        criteria.setParams(params);

        Tag actual = tagRepository.find(criteria);

        assertNull(actual);
    }
}