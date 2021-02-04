package com.epam.esm.service;

import com.epam.esm.service.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto findById(Long id);
}
