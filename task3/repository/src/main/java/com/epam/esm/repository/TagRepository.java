package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagRepository {
    List<Tag> findAll(Integer offset, Integer limit);
    Tag findById(Long id);
    Tag findByName(String name);
    Tag save(Tag tag);
    void delete(Tag tag);
}
