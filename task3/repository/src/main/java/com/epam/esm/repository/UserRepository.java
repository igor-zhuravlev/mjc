package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll(Integer offset, Integer limit);
    User findById(Long id);
}
