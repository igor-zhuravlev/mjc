package com.epam.esm.repository;

import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.User;

public interface TagRepository extends CrudRepository<Tag, Long> {
    Tag findByName(String name);
    Tag findMostWidelyUsedTagWithHighestCostOfOrdersByUser(User user);
}
