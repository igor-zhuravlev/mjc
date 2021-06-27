package com.epam.esm.repository;

import com.epam.esm.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryExtra {
    Tag findByName(String name);
}
