package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User findById(Long id);
}
