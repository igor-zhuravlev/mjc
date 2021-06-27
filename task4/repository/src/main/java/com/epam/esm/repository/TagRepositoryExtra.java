package com.epam.esm.repository;

import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.User;

public interface TagRepositoryExtra {
    Tag findMostWidelyUsedTagWithHighestCostOfOrdersByUser(User user);
}
