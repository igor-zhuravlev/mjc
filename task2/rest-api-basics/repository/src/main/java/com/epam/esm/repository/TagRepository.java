package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.exception.RepositoryException;

import java.util.List;

public interface TagRepository extends Repository<Tag, Long> {
    List<Tag> findAll() throws RepositoryException;
}
