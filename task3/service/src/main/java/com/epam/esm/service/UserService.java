package com.epam.esm.service;

import com.epam.esm.service.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll(Integer offset, Integer limit);
    UserDto findById(Long id);
}
